package api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface CryptoExchangeService {
	
	@GetMapping("/crypto-exchange/value")
	ResponseEntity<?> getCryptoValue(@RequestParam("crypto") String cryptoSymbol, @RequestParam("currency") String currency);

}
