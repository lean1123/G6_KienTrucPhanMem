package ktpm17ctt.g6.identity.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import ktpm17ctt.g6.identity.dto.ApiResponse;
import ktpm17ctt.g6.identity.dto.request.*;
import ktpm17ctt.g6.identity.dto.response.AuthenticationResponse;
import ktpm17ctt.g6.identity.dto.response.IntrospectResponse;
import ktpm17ctt.g6.identity.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @GetMapping("/social-login")
    ApiResponse<String> socialLogin(@RequestParam String provider) {
        provider = provider.trim().toLowerCase();
        String redirectUrl = authenticationService.generateSocialAuthenticationURL(provider);
        return ApiResponse.<String>builder().result(redirectUrl).build();
    }

    @GetMapping("/social-login/callback")
    ApiResponse<AuthenticationResponse> socialLoginCallback(@RequestParam String provider, @RequestParam String code) throws Exception {
        provider = provider.trim().toLowerCase();
        Map<String,Object> result = authenticationService.authenticationAndFetchProfile(provider, code);
        if (result == null) {
            return ApiResponse.<AuthenticationResponse>builder()
                    .code(404)
                    .message("User not found")
                    .build();
        }
        String email = (String) result.get("email");
        String firstName = (String) result.get("given_name");
        String lastName = (String) result.get("family_name");
        String avatar = (String) result.get("picture");
        String googleAccountId = (String) result.get("sub");

        LoginSocialRequest request = LoginSocialRequest.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .avatar(avatar)
                .googleAccountId(googleAccountId)
                .build();
        var authenticationResponse = authenticationService.loginSocial(request, provider);
        return ApiResponse.<AuthenticationResponse>builder().result(authenticationResponse).build();
    }
}
