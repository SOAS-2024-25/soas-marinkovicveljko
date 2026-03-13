package cryptoConversion;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.CryptoExchangeDto;
import api.dtos.CryptoWalletDto;
import api.dtos.UserDto;
import api.proxies.CryptoExchangeProxy;
import api.proxies.CryptoWalletProxy;
import api.proxies.UsersProxy;
import api.services.CryptoConversionService;

@RestController
public class CryptoConversionServiceImpl implements CryptoConversionService {

	@Autowired
	private UsersProxy usersProxy;

	@Autowired
	private CryptoWalletProxy walletProxy;

	@Autowired
	private CryptoExchangeProxy exchangeProxy;

	@Override
	public ResponseEntity<?> convertCrypto(String email, String fromSymbol, String toSymbol, BigDecimal amount) {

		UserDto user = usersProxy.getUserByEmail(email);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("User does not exist");
		}

		List<CryptoWalletDto> wallets = walletProxy.getWalletByEmail(email);

		if (wallets == null || wallets.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("User has no crypto wallets");
		}

		CryptoWalletDto fromWallet = null;
		CryptoWalletDto toWallet = null;

		for (CryptoWalletDto w : wallets) {
			if (w.getCryptoSymbol().equalsIgnoreCase(fromSymbol)) {
				fromWallet = w;
			}
			if (w.getCryptoSymbol().equalsIgnoreCase(toSymbol)) {
				toWallet = w;
			}
		}

		if (fromWallet == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("User does not own crypto: " + fromSymbol);
		}

		if (fromWallet.getBalance().compareTo(amount) < 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Insufficient crypto balance");
		}

		CryptoExchangeDto rateFrom = (CryptoExchangeDto) exchangeProxy.getCryptoValue(fromSymbol, "EUR").getBody();
		CryptoExchangeDto rateTo = (CryptoExchangeDto) exchangeProxy.getCryptoValue(toSymbol, "EUR").getBody();

		if (rateFrom == null || rateTo == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Exchange rate not available");
		}

		BigDecimal eurValue = rateFrom.getValue().multiply(amount);
		BigDecimal acquired = eurValue.divide(rateTo.getValue(), 8, BigDecimal.ROUND_HALF_UP);

		fromWallet.setBalance(fromWallet.getBalance().subtract(amount));
		walletProxy.updateWallet(
				fromWallet.getEmail(),
				fromWallet.getCryptoSymbol(),
				fromWallet
		);

		if (toWallet == null) {
			toWallet = new CryptoWalletDto(email, toSymbol, acquired);
			walletProxy.createWallet(toWallet);
		} else {
			toWallet.setBalance(toWallet.getBalance().add(acquired));
			walletProxy.updateWallet(
					toWallet.getEmail(),
					toWallet.getCryptoSymbol(),
					toWallet
			);
		}

		return ResponseEntity.ok("Crypto successfully converted");
	}
}