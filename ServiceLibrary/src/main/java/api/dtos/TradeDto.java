package api.dtos;

import java.math.BigDecimal;

public class TradeDto {

	private String email;
	private String cryptoSymbol;
	private BigDecimal amount;
	private String currency;
	private String type;
	
	public TradeDto() {
		
	}

	public TradeDto(String email, String cryptoSymbol, BigDecimal amount, String currency, String type) {
		this.email = email;
		this.cryptoSymbol = cryptoSymbol;
		this.amount = amount;
		this.currency = currency;
		this.type = type;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}
