package cryptoWallet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoWalletRepository extends JpaRepository<CryptoWalletModel, Integer> {

	List<CryptoWalletModel> findByEmail(String email);
	
	CryptoWalletModel findByEmailAndCryptoSymbol(String email, String cryptoSymbol);
}
