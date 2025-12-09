package tradeService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(basePackages = "api.proxies")
@ComponentScan(basePackages = { "tradeService", "api" })
public class TradeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeServiceApplication.class, args);
    }
}
