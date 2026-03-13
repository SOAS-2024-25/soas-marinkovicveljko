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
import api.dtos.FiatBalanceDto;
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
	    UserModel model = repo.findByEmail(email);
	    if (model == null) {
	        return null;
	    }
	    return convertModelToDto(model);
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
	    if (repo.findByEmail(dto.getEmail()) != null) {
	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body("User with passed email already exists");
	    }

	    dto.setRole("USER");
	    UserModel model = convertDtoToModel(dto);
	    repo.save(model);

	    try {
	        List<FiatBalanceDto> balances = new ArrayList<>();
	        balances.add(new FiatBalanceDto("EUR", BigDecimal.ZERO));
	        balances.add(new FiatBalanceDto("USD", BigDecimal.ZERO));
	        balances.add(new FiatBalanceDto("GBP", BigDecimal.ZERO));
	        balances.add(new FiatBalanceDto("CHF", BigDecimal.ZERO));
	        balances.add(new FiatBalanceDto("RSD", BigDecimal.ZERO));

	        BankAccountDto bankDto = new BankAccountDto(dto.getEmail(), balances);
	        ResponseEntity<?> bankResponse = bankAccountProxy.createAccount(bankDto);

	        if (!bankResponse.getStatusCode().is2xxSuccessful()) {
	            repo.deleteByEmail(dto.getEmail());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("User created, but bank account creation failed");
	        }

	        ResponseEntity<?> btcResponse =
	                cryptoWalletProxy.createWallet(new CryptoWalletDto(dto.getEmail(), "BTC", BigDecimal.ZERO));
	        ResponseEntity<?> ethResponse =
	                cryptoWalletProxy.createWallet(new CryptoWalletDto(dto.getEmail(), "ETH", BigDecimal.ZERO));
	        ResponseEntity<?> solResponse =
	                cryptoWalletProxy.createWallet(new CryptoWalletDto(dto.getEmail(), "SOL", BigDecimal.ZERO));

	        if (!btcResponse.getStatusCode().is2xxSuccessful()
	                || !ethResponse.getStatusCode().is2xxSuccessful()
	                || !solResponse.getStatusCode().is2xxSuccessful()) {

	            try {
	                bankAccountProxy.deleteAccount(dto.getEmail());
	            } catch (Exception e) {
	            }

	            repo.deleteByEmail(dto.getEmail());

	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body("User created, but crypto wallet creation failed");
	        }

	        return ResponseEntity.status(HttpStatus.CREATED).body(dto);

	    } catch (Exception e) {
	        try {
	            bankAccountProxy.deleteAccount(dto.getEmail());
	        } catch (Exception ex) {
	        }

	        try {
	            cryptoWalletProxy.deleteWallet(dto.getEmail());
	        } catch (Exception ex) {
	        }

	        repo.deleteByEmail(dto.getEmail());

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("User creation failed during automatic account/wallet provisioning: " + e.getMessage());
	    }
	}

	@Override
	public ResponseEntity<?> updateUser(String email, UserDto dto) {

	    UserModel existing = repo.findByEmail(email);
	    if (existing == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("User with passed email does not exist");
	    }

	    if ("OWNER".equalsIgnoreCase(dto.getRole())) {
	        long owners = repo.countByRoleIgnoreCase("OWNER");
	        boolean alreadyOwner = "OWNER".equalsIgnoreCase(existing.getRole());

	        if (owners > 0 && !alreadyOwner) {
	            return ResponseEntity.status(HttpStatus.CONFLICT)
	                    .body("Only one OWNER is allowed in the system");
	        }
	    }

	    repo.updateUser(email, dto.getPassword(), dto.getRole());

	    UserDto responseDto = new UserDto(email, dto.getPassword(), dto.getRole());
	    return ResponseEntity.ok(responseDto);
	}

	
	public UserDto convertModelToDto(UserModel model) {
	    if (model == null) {
	        return null;
	    }
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
