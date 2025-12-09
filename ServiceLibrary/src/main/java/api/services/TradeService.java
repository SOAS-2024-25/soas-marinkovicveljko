package api.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import api.dtos.TradeDto;

public interface TradeService {
	
	@PostMapping("/trade/buy")
	ResponseEntity<?> buyCrypto(@RequestBody TradeDto dto);
	
	@PostMapping("/trade/sell")
	ResponseEntity<?> sellCrypto(@RequestBody TradeDto dto);

}
