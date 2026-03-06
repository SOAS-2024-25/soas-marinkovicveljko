package cryptoExchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication( exclude = {
	        FeignAutoConfiguration.class,
	        org.springframework.cloud.autoconfigure.RefreshAutoConfiguration.class }
	)
	@ComponentScan(basePackages = { "cryptoExchange" })
public class CryptoExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoExchangeApplication.class, args);
	}

}
