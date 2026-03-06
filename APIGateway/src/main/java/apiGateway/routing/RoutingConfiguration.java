package apiGateway.routing;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfiguration {

    @Bean
    public RouteLocator gatewayRouting(RouteLocatorBuilder builder) {

        return builder.routes()

                // ===== ACTUATOR RUTE =====
                .route("users-actuator", r -> r
                        .path("/users/actuator/**")
                        .uri("lb://users-service"))

                // ===== API RUTE =====
                .route(r -> r.path("/users/**")
                        .uri("lb://users-service"))

                .route(r -> r.path("/bank-account/**")
                        .uri("lb://bank-account"))

                .route(r -> r.path("/crypto-exchange/**")
                        .uri("lb://crypto-exchange"))

                .route(r -> r.path("/crypto-wallet/**")
                        .uri("lb://crypto-wallet"))

                .route(r -> r.path("/currency-exchange/**")
                        .uri("lb://currency-exchange"))

                .route(r -> r.path("/currency-conversion/**")
                        .uri("lb://currency-conversion"))

                .route(r -> r.path("/crypto-conversion/**")
                        .uri("lb://crypto-conversion"))

                .route(r -> r.path("/trade/**")
                        .uri("lb://trade-service"))

                .build();
    }
}
