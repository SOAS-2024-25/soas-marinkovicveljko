package bankAccount;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "bank_account")
public class BankAccountModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_acc_seq")
	@SequenceGenerator(name = "bank_acc_seq", sequenceName = "bank_acc_seq", allocationSize = 1)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal eur;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal usd;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal gbp;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal chf;
	
	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal rsd;
	
	
	public BankAccountModel() {
		this.eur = BigDecimal.ZERO;
		this.usd = BigDecimal.ZERO;
		this.gbp = BigDecimal.ZERO;
		this.chf = BigDecimal.ZERO;
		this.rsd = BigDecimal.ZERO;
	}
	
	public BankAccountModel(String email) {
		this.email = email;
	}

	public BankAccountModel(String email, BigDecimal eur, BigDecimal usd, BigDecimal gbp, BigDecimal chf,
			BigDecimal rsd) {
		this.email = email;
		this.eur = eur;
		this.usd = usd;
		this.gbp = gbp;
		this.chf = chf;
		this.rsd = rsd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getEur() {
		return eur;
	}

	public void setEur(BigDecimal eur) {
		this.eur = eur;
	}

	public BigDecimal getUsd() {
		return usd;
	}

	public void setUsd(BigDecimal usd) {
		this.usd = usd;
	}

	public BigDecimal getGbp() {
		return gbp;
	}

	public void setGbp(BigDecimal gbp) {
		this.gbp = gbp;
	}

	public BigDecimal getChf() {
		return chf;
	}

	public void setChf(BigDecimal chf) {
		this.chf = chf;
	}

	public BigDecimal getRsd() {
		return rsd;
	}

	public void setRsd(BigDecimal rsd) {
		this.rsd = rsd;
	}
	

}
