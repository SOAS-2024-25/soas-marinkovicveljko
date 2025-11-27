package userService;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.BankAccountDto;
import api.dtos.CryptoWalletDto;
import api.dtos.UserDto;
import api.proxies.BankAccountProxy;
import api.proxies.CryptoWalletProxy;
import api.services.UsersService;

@RestController
public class UserServiceImpl implements UsersService {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;

	@Override
	public List<UserDto> getUsers() {
		List<UserModel> models = repo.findAll();
		List<UserDto> dtos = new ArrayList<UserDto>();
		for(UserModel model : models) {
			dtos.add(convertModelToDto(model));
		}
		return dtos;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		return convertModelToDto(repo.findByEmail(email));
	}

	@Override
	public ResponseEntity<?> createAdmin(UserDto dto) {
		if(repo.findByEmail(dto.getEmail()) == null) {
			dto.setRole("ADMIN");
			UserModel model = convertDtoToModel(dto);
			repo.save(model);
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("User with passed email already exist");
		}
	}

	@Override
	public ResponseEntity<?> createUser(UserDto dto) {
		if(repo.findByEmail(dto.getEmail()) == null) {
			
			dto.setRole("USER");
			UserModel model = convertDtoToModel(dto);
			repo.save(model);

			BankAccountDto bankDto =
					new BankAccountDto(dto.getEmail(), new ArrayList<>());
			bankAccountProxy.createAccount(bankDto);

			cryptoWalletProxy.createWallet(
					new CryptoWalletDto(dto.getEmail(), "BTC", BigDecimal.ZERO));
			cryptoWalletProxy.createWallet(
					new CryptoWalletDto(dto.getEmail(), "ETH", BigDecimal.ZERO));
			cryptoWalletProxy.createWallet(
					new CryptoWalletDto(dto.getEmail(), "SOL", BigDecimal.ZERO));

			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
			
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("User with passed email already exists");
		}
	}

	@Override
	public ResponseEntity<?> updateUser(UserDto dto) {
		if(repo.findByEmail(dto.getEmail()) != null) {
			repo.updateUser(dto.getEmail(), dto.getPassword(), dto.getRole());
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("User with passed email doesnt exists");
		}
	}
	
	public UserDto convertModelToDto(UserModel model) {
		return new UserDto(model.getEmail(), model.getPassword(), model.getRole());
	}
	
	public UserModel convertDtoToModel(UserDto dto) {
		return new UserModel(dto.getEmail(), dto.getPassword(), dto.getRole());
	}

	@Override
	public ResponseEntity<?> deleteUser(String email) {
	
		UserModel existing = repo.findByEmail(email);
		if(existing == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("User with passed email does not exist");
		}
		
		try {
			bankAccountProxy.deleteAccount(email);
		} catch (Exception e) {
		}

		try {
			cryptoWalletProxy.deleteWallet(email);
		} catch (Exception e) {
		}
		
		repo.deleteByEmail(email);
		
		return ResponseEntity.ok(
				"User and his wallets removed: " + email);
	}
}
