package api.dtos;

import java.math.BigDecimal;

public class CryptoExchangeDto {
	
	private String cryptoSymbol;
	private String currency;
	private BigDecimal value;
	
	public CryptoExchangeDto() {
		
	}

	public CryptoExchangeDto(String cryptoSymbol, String currency, BigDecimal value) {
		this.cryptoSymbol = cryptoSymbol;
		this.currency = currency;
		this.value = value;
	}



	public String getCryptoSymbol() {
		return cryptoSymbol;
	}

	public void setCryptoSymbol(String cryptoSymbol) {
		this.cryptoSymbol = cryptoSymbol;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	

}
