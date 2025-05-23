package ktpm17ctt.g6.gateway.configuration;

import ktpm17ctt.g6.gateway.repository.IdentityClient;
import ktpm17ctt.g6.gateway.repository.ProductClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;
import java.util.List;

@Configuration
public class WebClientConfiguration {

    @Value("#{'${app.cors.allowed-origins:*}'.split(',')}")
    private List<String> allowedOrigins;

    @Value("${IDENTITY_SERVICE_URL:http://localhost:8080/identity}")
    private String identityServiceUrl;
    @Value("${PRODUCT_SERVICE_URL:http://localhost:8082/product}")
    private String productServiceUrl;

    @Bean
    WebClient identityWebClient() {
        return WebClient.builder()
                .baseUrl(identityServiceUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .defaultHeaders(headers -> headers.add("Content-Type", "application/json"))
                .filter((request, next) -> next.exchange(request)
                        .timeout(Duration.ofSeconds(5)).retry(3))
                .build();
    }

    @Bean
    WebClient productWebClient() {
        return WebClient.builder()
                .baseUrl(productServiceUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configure -> configure.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .defaultHeaders(headers -> headers.add("Content-Type", "application/json"))
                .filter((request, next) -> next.exchange(request)
                        .timeout(Duration.ofSeconds(5)).retry(3))
                .build();
    }

    @Bean
    CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(allowedOrigins);
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }

    @Bean
    IdentityClient identityClient(WebClient identityWebClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(identityWebClient)).build();
        return httpServiceProxyFactory.createClient(IdentityClient.class);
    }

    @Bean
    ProductClient productClient(WebClient productWebClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(productWebClient)).build();
        return httpServiceProxyFactory.createClient(ProductClient.class);
    }
}
