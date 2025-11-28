package cryptoExchange;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoExchangeRepository extends JpaRepository<CryptoExchangeModel, Integer> {

	CryptoExchangeModel findByCryptoSymbolAndCurrency(String cryptoSymbol, String currency);

	List<CryptoExchangeModel> findByCryptoSymbol(String cryptoSymbol);
}
