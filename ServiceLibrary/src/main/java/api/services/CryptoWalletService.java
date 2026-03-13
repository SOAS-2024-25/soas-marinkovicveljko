package api.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import api.dtos.CryptoWalletDto;

public interface CryptoWalletService {
	
	@GetMapping("/crypto-wallets")
	List<CryptoWalletDto> getWallets();
	
	@GetMapping("/crypto-wallets/email")
	List<CryptoWalletDto> getWalletByEmail(@RequestParam("email") String email);
	
	@PostMapping("/crypto-wallets")
	ResponseEntity<?> createWallet(@RequestBody CryptoWalletDto dto);
	
	@PutMapping("/crypto-wallets/{email}/{cryptoSymbol}")
	ResponseEntity<?> updateWallet(@PathVariable String email, @PathVariable String cryptoSymbol, @RequestBody CryptoWalletDto dto);
	
	@DeleteMapping("/crypto-wallets/email")
	ResponseEntity<?> deleteWallet(@RequestParam("email") String email);
	
	

}
