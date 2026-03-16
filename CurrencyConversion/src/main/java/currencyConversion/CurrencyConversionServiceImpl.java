package currencyConversion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.BankAccountDto;
import api.dtos.CurrencyConversionDto;
import api.dtos.CurrencyExchangeDto;
import api.dtos.FiatBalanceDto;
import api.proxies.BankAccountProxy;
import api.proxies.CurrencyExchangeProxy;
import api.services.CurrencyConversionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import util.exceptions.InvalidQuantityException;

@RestController
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

	@Autowired
	private CurrencyExchangeProxy proxy;

	@Autowired
	private BankAccountProxy bankAccountProxy;

	private Retry retry;
	private CurrencyExchangeDto response;

	public CurrencyConversionServiceImpl(RetryRegistry registry) {
		this.retry = registry.retry("default");
	}

	@Override
	@CircuitBreaker(name = "cb", fallbackMethod = "fallback")
	public ResponseEntity<?> getConversion(String email, String from, String to, BigDecimal quantity) {

		if (quantity.compareTo(BigDecimal.valueOf(300.0)) > 0) {
			throw new InvalidQuantityException(String.format("Quantity of %s is too large", quantity));
		}

		BankAccountDto account = bankAccountProxy.getAccountByEmail(email);
		if (account == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Bank account for passed email does not exist");
		}

		BigDecimal fromBalance = getBalance(account, from);

		if (fromBalance.compareTo(quantity) < 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Insufficient balance for conversion");
		}

		CurrencyExchangeDto exchange = proxy.getExchangeFeign(from, to).getBody();
		if (exchange == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Exchange rate not found");
		}

		BigDecimal convertedAmount = quantity.multiply(exchange.getExchangeRate());

		decreaseBalance(account, from, quantity);
		increaseBalance(account, to, convertedAmount);

		bankAccountProxy.updateAccount(account);

		CurrencyConversionDto result = new CurrencyConversionDto(exchange, quantity);
		result.setFeign(false);

		return ResponseEntity.ok(result);
	}

	@Override
	@CircuitBreaker(name = "cb", fallbackMethod = "fallbackFeign")
	public ResponseEntity<?> getConversionFeign(String email, String from, String to, BigDecimal quantity) {

		if (quantity.compareTo(BigDecimal.valueOf(300.0)) > 0) {
			throw new InvalidQuantityException(String.format("Quantity of %s is too large", quantity));
		}

		BankAccountDto account = bankAccountProxy.getAccountByEmail(email);
		if (account == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Bank account for passed email does not exist");
		}

		BigDecimal fromBalance = getBalance(account, from);

		if (fromBalance.compareTo(quantity) < 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Insufficient balance for conversion");
		}

		retry.executeSupplier(() -> response = proxy.getExchangeFeign(from, to).getBody());

		if (response == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Exchange rate not found");
		}

		BigDecimal convertedAmount = quantity.multiply(response.getExchangeRate());

		decreaseBalance(account, from, quantity);
		increaseBalance(account, to, convertedAmount);

		bankAccountProxy.updateAccount(account);

		CurrencyConversionDto finalResponse = new CurrencyConversionDto(response, quantity);
		finalResponse.setFeign(true);

		return ResponseEntity.ok(finalResponse);
	}

	public ResponseEntity<?> fallback(String email, String from, String to, BigDecimal quantity, Throwable ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Currency conversion service is currently unavailable. Circuit breaker fallback activated.");
	}

	public ResponseEntity<?> fallbackFeign(String email, String from, String to, BigDecimal quantity, Throwable ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Currency conversion FEIGN service is currently unavailable. Circuit breaker fallback activated.");
	}

	private BigDecimal getBalance(BankAccountDto account, String currency) {
		for (FiatBalanceDto balance : account.getFiatBalances()) {
			if (balance.getCurrency().equalsIgnoreCase(currency)) {
				return balance.getAmount();
			}
		}
		return BigDecimal.ZERO;
	}

	private void decreaseBalance(BankAccountDto account, String currency, BigDecimal amount) {
		for (FiatBalanceDto balance : account.getFiatBalances()) {
			if (balance.getCurrency().equalsIgnoreCase(currency)) {
				balance.setAmount(balance.getAmount().subtract(amount));
			}
		}
	}

	private void increaseBalance(BankAccountDto account, String currency, BigDecimal amount) {
		for (FiatBalanceDto balance : account.getFiatBalances()) {
			if (balance.getCurrency().equalsIgnoreCase(currency)) {
				balance.setAmount(balance.getAmount().add(amount));
			}
		}
	}
}