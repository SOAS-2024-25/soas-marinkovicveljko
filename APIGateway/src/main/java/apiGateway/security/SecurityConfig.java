package apiGateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
public class SecurityConfig {

    // 🔓 ACTUATOR – BEZ AUTH
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

    // 🔐 SVE OSTALO – BASIC AUTH
    @Bean
    @Order(2)
    public SecurityWebFilterChain apiSecurity(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(basic -> {})
                .authorizeExchange(ex -> ex
                        .pathMatchers("/users/**")
                        .hasAnyRole("ADMIN", "OWNER")
                        .anyExchange()
                        .authenticated()
                )
                .build();
    }
}
