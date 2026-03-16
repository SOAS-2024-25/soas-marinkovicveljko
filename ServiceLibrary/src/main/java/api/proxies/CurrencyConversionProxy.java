package api.proxies;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("currency-conversion")
public interface CurrencyConversionProxy {

    @GetMapping("/currency-conversion")
    ResponseEntity<?> getConversion(
            @RequestParam("email") String email,
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @RequestParam("quantity") BigDecimal quantity
    );
}