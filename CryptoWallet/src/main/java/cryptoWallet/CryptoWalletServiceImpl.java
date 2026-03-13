package cryptoWallet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.CryptoWalletDto;
import api.services.CryptoWalletService;

@RestController
public class CryptoWalletServiceImpl implements CryptoWalletService {

	
	@Autowired
	CryptoWalletRepository repo;
	
	
	@Override
	public List<CryptoWalletDto> getWallets() {
		List<CryptoWalletModel> models = repo.findAll();
		List<CryptoWalletDto> dtos = new ArrayList<>();
		for(CryptoWalletModel model : models) {
			dtos.add(convertModelToDto(model));
		}
		return dtos;
	}

	@Override
	public List<CryptoWalletDto> getWalletByEmail(String email) {
		List<CryptoWalletModel> models = repo.findByEmail(email);
		List<CryptoWalletDto> dtos = new ArrayList<>();
		for(CryptoWalletModel model : models) {
			dtos.add(convertModelToDto(model));
		}
		return dtos;
	}

	@Override
	public ResponseEntity<?> createWallet(CryptoWalletDto dto) {
		if(repo.findByEmailAndCryptoSymbol(dto.getEmail(), dto.getCryptoSymbol()) == null) {
			CryptoWalletModel model = convertDtoToModel(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(model));
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Wallet for currency " + dto.getCryptoSymbol() + " already exists for user " + dto.getEmail());
		}
	}

	@Override
	public ResponseEntity<?> updateWallet(String email, String cryptoSymbol, CryptoWalletDto dto) {
	    CryptoWalletModel model = repo.findByEmailAndCryptoSymbol(email, cryptoSymbol);

	    if (model != null) {
	        model.setBalance(dto.getBalance());
	        return ResponseEntity.status(HttpStatus.OK).body(repo.save(model));
	    } else {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body("Wallet for given email and currency does not exist");
	    }
	}

	@Override
	public ResponseEntity<?> deleteWallet(String email) {
		List<CryptoWalletModel> models = repo.findByEmail(email);
		if(!models.isEmpty()) {
			for(CryptoWalletModel m : models) {
				repo.delete(m);
			}
			return ResponseEntity.status(HttpStatus.OK).body("Deleted wallet for email: " + email);
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Wallet for passed email does not exist");
		}
	}
	
	
	private CryptoWalletDto convertModelToDto(CryptoWalletModel model) {
		return new CryptoWalletDto(
				model.getEmail(),
				model.getCryptoSymbol(),
				model.getBalance()
				);
	}
	
	private CryptoWalletModel convertDtoToModel(CryptoWalletDto dto) {
		return new CryptoWalletModel(
				0,
				dto.getEmail(),
				dto.getCryptoSymbol(),
				dto.getBalance()
				);
	}

}
