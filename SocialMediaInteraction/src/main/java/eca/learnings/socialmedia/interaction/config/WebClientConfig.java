package eca.learnings.socialmedia.interaction.config;

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
}
