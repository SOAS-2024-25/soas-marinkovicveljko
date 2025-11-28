package bankAccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dtos.BankAccountDto;
import api.dtos.FiatBalanceDto;
import api.dtos.UserDto;
import api.proxies.UsersProxy;
import api.services.BankAccountService;

@RestController
public class BankAccountServiceImpl implements BankAccountService {

	@Autowired
	private BankAccountRepository repo;
	
	@Autowired 
	UsersProxy usersProxy;
	
	@Override
	public List<BankAccountDto> getAllAcounts() {
		List<BankAccountModel> models = repo.findAll();
		List<BankAccountDto> dtos = new ArrayList<>();
		for(BankAccountModel m : models) {
			dtos.add(convertModelToDto(m));
		}
		return dtos;
	}

	@Override
	public BankAccountDto getAccountByEmail(String email) {
		BankAccountModel model = repo.findByEmail(email);
		if (model == null) {
			return null;
		}
		return convertModelToDto(model);
	}

	@Override
	public ResponseEntity<?> createAccount(BankAccountDto dto) {
		
		String email = dto.getEmail();
		
		if(repo.existsByEmail(email)) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Bank account with passed email already exists");
		}
		
		UserDto user = usersProxy.getUserByEmail(email);
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("User with passed email does not exist");
		}
		if(!"USER".equalsIgnoreCase(user.getRole())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Bank account can be created only for USER role");
		}
		
		BankAccountModel model = convertDtoToModel(dto);
		ensureNotNullBalances(model);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(repo.save(model));
	}

	@Override
	public ResponseEntity<?> updateAccount(BankAccountDto dto) {
		
		BankAccountModel existing = repo.findByEmail(dto.getEmail());
		if (existing == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Bank account with passed email does not exist");
		}
		
		BankAccountModel updated = convertDtoToModel(dto);
		updated.setId(existing.getId());
		ensureNotNullBalances(updated);
		
		repo.save(updated);
		return ResponseEntity.ok(convertModelToDto(updated));
	}

	@Override
	public ResponseEntity<?> deleteAccount(String email) {
		
		BankAccountModel existing = repo.findByEmail(email);
		if(existing == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Bank account with passed email does not exist");
		}
		
		repo.deleteByEmail(email);
		return ResponseEntity.ok("Bank account succesfully deleted for email: " + email);
	}
	
	
	private BankAccountDto convertModelToDto(BankAccountModel model) {
	
		List<FiatBalanceDto> balances = new ArrayList<>();
		balances.add(new FiatBalanceDto("EUR", model.getEur()));
		balances.add(new FiatBalanceDto("USD", model.getUsd()));
		balances.add(new FiatBalanceDto("GBP", model.getGbp()));
		balances.add(new FiatBalanceDto("CHF", model.getChf()));
		balances.add(new FiatBalanceDto("RSD", model.getRsd()));
		
		return new BankAccountDto(model.getEmail(), balances);
	}
	
	private BankAccountModel convertDtoToModel(BankAccountDto dto) {
	
		BankAccountModel model = new BankAccountModel(dto.getEmail());
		
		if(dto.getFiatBalances()!=null) {
			for(FiatBalanceDto b : dto.getFiatBalances()) {
				setBalance(model, b.getCurrency(), b.getAmount());
			}
		}
		
		return model;
	}
	
	private void setBalance(BankAccountModel model, String currency, BigDecimal amount) {
		
		if(currency == null) return;
		if(amount == null) amount = BigDecimal.ZERO;
		
		
		switch(currency.toUpperCase()) {
		case "EUR" -> model.setEur(amount);
		case "USD" -> model.setUsd(amount);
		case "GBP" -> model.setGbp(amount);
		case "CHF" -> model.setChf(amount);
		case "RSD" -> model.setRsd(amount);
		default -> { }
		}
	}
	
	private void ensureNotNullBalances(BankAccountModel model) {
		if(model.getEur() == null) model.setEur(BigDecimal.ZERO);
		if(model.getUsd() == null) model.setUsd(BigDecimal.ZERO);
		if(model.getGbp() == null) model.setGbp(BigDecimal.ZERO);
		if(model.getChf() == null) model.setChf(BigDecimal.ZERO);
		if(model.getRsd() == null) model.setRsd(BigDecimal.ZERO);
	}

}
