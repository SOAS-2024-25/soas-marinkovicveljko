package cryptoExchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.CryptoExchangeDto;
import api.services.CryptoExchangeService;

@RestController
public class CryptoExchangeServiceImpl  implements CryptoExchangeService {

	@Autowired
	private CryptoExchangeRepository repo;
	
	
	@Override
	public ResponseEntity<?> getCryptoValue(String cryptoSymbol, String currency) {
	
		CryptoExchangeModel model = repo.findByCryptoSymbolAndCurrency(cryptoSymbol, currency);
	
		if(model != null) {
			CryptoExchangeDto dto = new CryptoExchangeDto(
					model.getCryptoSymbol(),
					model.getCurrency(),
					model.getExchangeValue()
					);
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)	
					.body("Crypto value not found for given parameters");
					}
	}

}
