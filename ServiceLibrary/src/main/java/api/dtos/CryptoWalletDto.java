package api.dtos;

import java.math.BigDecimal;

public class CryptoWalletDto {
	
	private String email;
	private String cryptoSymbol;
	private BigDecimal balance;
	
	public CryptoWalletDto() {
		
	}

	public CryptoWalletDto(String email, String cryptoSymbol, BigDecimal balance) {
		this.email = email;
		this.cryptoSymbol = cryptoSymbol;
		this.balance = balance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCryptoSymbol() {
		return cryptoSymbol;
	}

	public void setCryptoSymbol(String cryptoSymbol) {
		this.cryptoSymbol = cryptoSymbol;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
	
	

}
