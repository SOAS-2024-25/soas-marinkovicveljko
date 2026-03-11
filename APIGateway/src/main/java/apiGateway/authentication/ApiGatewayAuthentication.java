package apiGateway.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import api.dtos.UserDto;
import reactor.core.publisher.Mono;

@Service
public class ApiGatewayAuthentication implements ReactiveUserDetailsService {

	
    private final WebClient webClient;

    @Autowired
    public ApiGatewayAuthentication(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("lb://USERS-SERVICE")
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/email")
                        .queryParam("email", username)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(UserDto.class)
                .map(dto -> User.withUsername(dto.getEmail())
                        .password("{noop}" + dto.getPassword())
                        .roles(dto.getRole())
                        .build());
    }
}
