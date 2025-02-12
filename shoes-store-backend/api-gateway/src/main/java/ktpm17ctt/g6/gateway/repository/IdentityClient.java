package ktpm17ctt.g6.gateway.repository;

import ktpm17ctt.g6.gateway.dto.ApiResponse;
import ktpm17ctt.g6.gateway.dto.request.IntrospectRequest;
import ktpm17ctt.g6.gateway.dto.response.IntrospectResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange(value = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspectToken(@RequestBody IntrospectRequest request);
}
