package api.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("crypto-exchange")
public interface CryptoExchangeProxy {

	@GetMapping("/crypto-exchange/value")
	ResponseEntity<?> getCryptoValue(@RequestParam("crypto") String cryptoSymbol, @RequestParam("currency") String currency);
}
