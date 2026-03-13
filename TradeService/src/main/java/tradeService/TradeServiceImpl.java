package tradeService;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.BankAccountDto;
import api.dtos.CryptoExchangeDto;
import api.dtos.CryptoWalletDto;
import api.dtos.FiatBalanceDto;
import api.dtos.TradeDto;
import api.proxies.BankAccountProxy;
import api.proxies.CryptoExchangeProxy;
import api.proxies.CryptoWalletProxy;
import api.proxies.UsersProxy;
import api.services.TradeService;

@RestController
public class TradeServiceImpl implements TradeService {

	@Autowired
	private UsersProxy usersProxy;

	@Autowired
	private BankAccountProxy bankAccountProxy;

	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;

	@Autowired
	private CryptoExchangeProxy cryptoExchangeProxy;

	@Override
	public ResponseEntity<?> buyCrypto(TradeDto dto) {

		if (usersProxy.getUserByEmail(dto.getEmail()) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("User does not exist");
		}

		ResponseEntity<?> cryptoResponse =
				cryptoExchangeProxy.getCryptoValue(dto.getCryptoSymbol(), dto.getCurrency());

		if (!cryptoResponse.getStatusCode().is2xxSuccessful() || cryptoResponse.getBody() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Crypto exchange rate not found");
		}

		CryptoExchangeDto crypto = (CryptoExchangeDto) cryptoResponse.getBody();
		BigDecimal totalPrice = crypto.getValue().multiply(dto.getAmount());

		BankAccountDto bank = bankAccountProxy.getAccountByEmail(dto.getEmail());
		BigDecimal balance = getFiat(bank, dto.getCurrency());

		if (balance.compareTo(totalPrice) < 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Insufficient bank account balance");
		}

		decreaseFiat(bank, dto.getCurrency(), totalPrice);
		bankAccountProxy.updateAccount(bank);

		List<CryptoWalletDto> wallets =
				cryptoWalletProxy.getWalletByEmail(dto.getEmail());

		CryptoWalletDto wallet = wallets.stream()
				.filter(w -> w.getCryptoSymbol().equalsIgnoreCase(dto.getCryptoSymbol()))
				.findFirst()
				.orElse(null);

		if (wallet == null) {
			wallet = new CryptoWalletDto(
					dto.getEmail(),
					dto.getCryptoSymbol(),
					dto.getAmount());
			cryptoWalletProxy.createWallet(wallet);
		} else {
			wallet.setBalance(wallet.getBalance().add(dto.getAmount()));
			cryptoWalletProxy.updateWallet(
					wallet.getEmail(),
					wallet.getCryptoSymbol(),
					wallet
			);
		}

		return ResponseEntity.ok("Crypto bought successfully");
	}

	@Override
	public ResponseEntity<?> sellCrypto(TradeDto dto) {

		if (usersProxy.getUserByEmail(dto.getEmail()) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("User does not exist");
		}

		List<CryptoWalletDto> wallets =
				cryptoWalletProxy.getWalletByEmail(dto.getEmail());

		CryptoWalletDto wallet = wallets.stream()
				.filter(w -> w.getCryptoSymbol().equalsIgnoreCase(dto.getCryptoSymbol()))
				.findFirst()
				.orElse(null);

		if (wallet == null || wallet.getBalance().compareTo(dto.getAmount()) < 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Insufficient crypto balance");
		}

		ResponseEntity<?> cryptoResponse =
				cryptoExchangeProxy.getCryptoValue(dto.getCryptoSymbol(), dto.getCurrency());

		if (!cryptoResponse.getStatusCode().is2xxSuccessful() || cryptoResponse.getBody() == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Crypto exchange rate not found");
		}

		CryptoExchangeDto crypto = (CryptoExchangeDto) cryptoResponse.getBody();
		BigDecimal totalValue = crypto.getValue().multiply(dto.getAmount());

		wallet.setBalance(wallet.getBalance().subtract(dto.getAmount()));
		cryptoWalletProxy.updateWallet(
				wallet.getEmail(),
				wallet.getCryptoSymbol(),
				wallet
		);

		BankAccountDto bank = bankAccountProxy.getAccountByEmail(dto.getEmail());
		increaseFiat(bank, dto.getCurrency(), totalValue);
		bankAccountProxy.updateAccount(bank);

		return ResponseEntity.ok("Crypto sold successfully");
	}

	private BigDecimal getFiat(BankAccountDto bank, String currency) {
		for (FiatBalanceDto f : bank.getFiatBalances()) {
			if (f.getCurrency().equalsIgnoreCase(currency)) {
				return f.getAmount();
			}
		}
		return BigDecimal.ZERO;
	}

	private void decreaseFiat(BankAccountDto bank, String currency, BigDecimal amount) {
		for (FiatBalanceDto f : bank.getFiatBalances()) {
			if (f.getCurrency().equalsIgnoreCase(currency)) {
				f.setAmount(f.getAmount().subtract(amount));
			}
		}
	}

	private void increaseFiat(BankAccountDto bank, String currency, BigDecimal amount) {
		for (FiatBalanceDto f : bank.getFiatBalances()) {
			if (f.getCurrency().equalsIgnoreCase(currency)) {
				f.setAmount(f.getAmount().add(amount));
			}
		}
	}
}