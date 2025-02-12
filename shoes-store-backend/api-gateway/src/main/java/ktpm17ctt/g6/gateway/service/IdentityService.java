package ktpm17ctt.g6.gateway.service;

import ktpm17ctt.g6.gateway.dto.ApiResponse;
import ktpm17ctt.g6.gateway.dto.request.IntrospectRequest;
import ktpm17ctt.g6.gateway.dto.response.IntrospectResponse;
import ktpm17ctt.g6.gateway.repository.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspectToken(String token) {
        return identityClient.introspectToken(IntrospectRequest.builder().token(token).build());
    }
}
