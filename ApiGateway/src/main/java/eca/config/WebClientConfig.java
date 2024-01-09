package eca.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(HttpClient.create()));
    }
    @Bean
    public WebClient userDataWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("http://user-client") // Service name registered with Eureka
                .build();
    }
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/myapp/users/**") // Define the path pattern to route to the UserData service
                        .uri("lb://user-client") // Service name registered with Eureka
                )
                .build();
    }
}
