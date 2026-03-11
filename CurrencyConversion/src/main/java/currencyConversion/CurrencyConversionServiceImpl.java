package currencyConversion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import api.dtos.CurrencyConversionDto;
import api.dtos.CurrencyExchangeDto;
import api.proxies.CurrencyExchangeProxy;
import api.services.CurrencyConversionService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import util.exceptions.InvalidQuantityException;

@RestController
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

	@Autowired
	private RestTemplate template;

	@Autowired
	private CurrencyExchangeProxy proxy;

	private Retry retry;
	private CurrencyExchangeDto response;

	public CurrencyConversionServiceImpl(RetryRegistry registry) {
		this.retry = registry.retry("default");
	}

	@Override
	@CircuitBreaker(name = "cb", fallbackMethod = "fallbackFeign")
	public ResponseEntity<?> getConversionFeign(String from, String to, BigDecimal quantity) {

		if (quantity.compareTo(BigDecimal.valueOf(300.0)) > 0) {
			throw new InvalidQuantityException(String.format("Quantity of %s is too large", quantity));
		}

		retry.executeSupplier(() -> response = proxy.getExchangeFeign(from, to).getBody());

		CurrencyConversionDto finalResponse = new CurrencyConversionDto(response, quantity);
		finalResponse.setFeign(true);

		return ResponseEntity.ok(finalResponse);
	}

	@Override
	@CircuitBreaker(name = "cb", fallbackMethod = "fallback")
	public ResponseEntity<?> getConversion(String from, String to, BigDecimal quantity) {

		if (quantity.compareTo(BigDecimal.valueOf(300.0)) > 0) {
			throw new InvalidQuantityException(String.format("Quantity of %s is too large", quantity));
		}

		String endPoint = "http://currency-exchange/currency-exchange?from=" + from + "&to=" + to;

		ResponseEntity<CurrencyExchangeDto> response =
				template.getForEntity(endPoint, CurrencyExchangeDto.class);

		return ResponseEntity.ok(new CurrencyConversionDto(response.getBody(), quantity));
	}

	public ResponseEntity<?> fallback(String from, String to, BigDecimal quantity, Throwable ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Currency conversion service is currently unavailable. Circuit breaker fallback activated.");
	}

	public ResponseEntity<?> fallbackFeign(String from, String to, BigDecimal quantity, Throwable ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Currency conversion FEIGN service is currently unavailable. Circuit breaker fallback activated.");
	}
}