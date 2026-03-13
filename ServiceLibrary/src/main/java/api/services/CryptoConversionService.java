package api.services;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface CryptoConversionService {
	
	@PostMapping("/crypto-conversion/convert")
	ResponseEntity<?> convertCrypto(
			@RequestParam("email") String email,
			@RequestParam("fromSymbol") String fromSymbol,
			@RequestParam("toSymbol") String toSymbol,
			@RequestParam("amount") BigDecimal amount);
}
