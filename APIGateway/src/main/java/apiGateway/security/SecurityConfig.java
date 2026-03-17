package apiGateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityWebFilterChain actuatorSecurity(ServerHttpSecurity http) {

        return http
                .securityMatcher(
                        ServerWebExchangeMatchers.pathMatchers(
                                "/actuator/**",
                                "/*/actuator/**"
                        )
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex.anyExchange().permitAll())
                .build();
    }

    @Bean
    @Order(2)
    public SecurityWebFilterChain apiSecurity(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(basic -> {})
                .authorizeExchange(ex -> ex

                        .pathMatchers(HttpMethod.GET, "/users")
                        .hasAnyRole("OWNER", "ADMIN")

                        .pathMatchers(HttpMethod.GET, "/users/email")
                        .hasAnyRole("OWNER", "ADMIN")

                        .pathMatchers(HttpMethod.POST, "/users/newAdmin")
                        .hasRole("OWNER")

                        .pathMatchers(HttpMethod.POST, "/users/newUser")
                        .hasAnyRole("OWNER", "ADMIN")

                        .pathMatchers(HttpMethod.PUT, "/users/email/**")
                        .hasAnyRole("OWNER", "ADMIN")

                        .pathMatchers(HttpMethod.DELETE, "/users/email")
                        .hasRole("OWNER")

                        .pathMatchers(HttpMethod.GET, "/currency-exchange/**")
                        .hasAnyRole("OWNER", "ADMIN", "USER")

                        .pathMatchers(HttpMethod.GET, "/crypto-exchange/**")
                        .hasAnyRole("OWNER", "ADMIN", "USER")

                        .pathMatchers(HttpMethod.GET, "/currency-conversion")
                        .hasRole("USER")

                        .pathMatchers(HttpMethod.GET, "/currency-conversion-feign")
                        .hasRole("USER")

                        .pathMatchers(HttpMethod.POST, "/trade/buy")
                        .hasRole("USER")

                        .pathMatchers(HttpMethod.POST, "/trade/sell")
                        .hasRole("USER")

                        .pathMatchers(HttpMethod.POST, "/crypto-conversion/convert")
                        .hasRole("USER")

                        .pathMatchers(HttpMethod.GET, "/crypto-wallets")
                        .hasRole("ADMIN")

                        .pathMatchers(HttpMethod.GET, "/crypto-wallets/email")
                        .hasAnyRole("ADMIN", "USER")

                        .pathMatchers(HttpMethod.POST, "/crypto-wallets")
                        .hasRole("ADMIN")

                        .pathMatchers(HttpMethod.PUT, "/crypto-wallets/**")
                        .hasRole("ADMIN")

                        .pathMatchers(HttpMethod.DELETE, "/crypto-wallets/email")
                        .hasRole("ADMIN")

                        .pathMatchers(HttpMethod.GET, "/bank-accounts")
                        .hasRole("ADMIN")

                        .pathMatchers(HttpMethod.GET, "/bank-accounts/email")
                        .hasAnyRole("ADMIN", "USER")

                        .pathMatchers(HttpMethod.POST, "/bank-accounts")
                        .hasRole("ADMIN")

                        .pathMatchers(HttpMethod.PUT, "/bank-accounts")
                        .hasRole("ADMIN")

                        .pathMatchers(HttpMethod.DELETE, "/bank-accounts/email")
                        .hasRole("ADMIN")

                        .anyExchange()
                        .authenticated()
                )
                .build();
    }
}