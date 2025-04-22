package ktpm17ctt.g6.identity.service;

import com.nimbusds.jose.JOSEException;
import ktpm17ctt.g6.identity.dto.request.*;
import ktpm17ctt.g6.identity.dto.response.AuthenticationResponse;
import ktpm17ctt.g6.identity.dto.response.IntrospectResponse;

import java.text.ParseException;

public interface AuthenticationService {
    IntrospectResponse introspect(IntrospectRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
    AuthenticationResponse loginSocial(LoginSocialRequest request, String provider) throws ParseException, JOSEException;
}
