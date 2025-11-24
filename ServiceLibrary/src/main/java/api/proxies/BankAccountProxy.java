package api.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.BankAccountDto;

@FeignClient("bank-account")
public interface BankAccountProxy {
	
	@GetMapping("/bank-accounts/email")
	BankAccountDto getAccountByEmail(@RequestParam("email") String email);
	
	@PostMapping("/bank-accounts")
	ResponseEntity<?> createAccount(@RequestBody BankAccountDto dto);
	
	@PutMapping("/bank-accounts")
	ResponseEntity<?> updateAccount(@RequestBody BankAccountDto dto);
	
	@DeleteMapping("/bank-accounts/email")
	ResponseEntity<?> deleteAccount(@RequestParam("email") String email);

}
