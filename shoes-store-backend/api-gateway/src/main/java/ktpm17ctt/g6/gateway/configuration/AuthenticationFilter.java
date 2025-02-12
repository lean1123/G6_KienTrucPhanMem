package ktpm17ctt.g6.gateway.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import ktpm17ctt.g6.gateway.dto.ApiResponse;
import ktpm17ctt.g6.gateway.service.IdentityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    private String[] publicEndpoints = new String[] {
        "/identity/auth/.*",
        "/identity/users/registration",
        "/notification/email/send"
    };

    @Value("${app.api-prefix}")
    @NonFinal
    private String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Entering authentication filter");

        if (isPublicEndpoint(exchange.getRequest())) {
            return chain.filter(exchange);
        }

        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isEmpty()) {
            return unauthorized(exchange.getResponse());
        }

        String token = authHeader.get(0).replace("Bearer ", "");

        return identityService.introspectToken(token).flatMap(response -> {
            if (response.getResult().isValid()) {
                return chain.filter(exchange);
            }
            return unauthorized(exchange.getResponse());
        }).onErrorResume(e -> unauthorized(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        return Arrays.stream(publicEndpoints).anyMatch(endpoint -> request.getURI().getPath().matches(apiPrefix + endpoint));
    }

    Mono<Void> unauthorized(ServerHttpResponse response) {
        ApiResponse<?> apiResponse = ApiResponse.builder().code(1401).message("Unauthorized").build();
        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiResponse);
        } catch (Exception e) {
            log.error("Error while writing response", e);
            throw new RuntimeException("Error while writing response");
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
