package apiGateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(basic -> {})
                .authorizeExchange(exchange -> exchange

                        .pathMatchers("/users/**")
                        .hasAnyRole("OWNER", "ADMIN")

                        .anyExchange()
                        .authenticated()
                )
                .build();
    }
}
