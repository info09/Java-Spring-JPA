package com.learning.identity_server.service;

import com.learning.identity_server.dto.request.AuthRequest;
import com.learning.identity_server.dto.request.IntrospectRequest;
import com.learning.identity_server.dto.response.AuthResponse;
import com.learning.identity_server.dto.response.IntrospectResponse;
import com.learning.identity_server.entity.User;
import com.learning.identity_server.exception.AppException;
import com.learning.identity_server.exception.ErrorCode;
import com.learning.identity_server.repository.IUserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    IUserRepository _userRepository;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthResponse Authenticated(@NotNull AuthRequest request) {
        var user = _userRepository.findByuserName(request.getUserName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var passwordEncoder = new BCryptPasswordEncoder(10);

        var authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);
        return AuthResponse.builder().isAuthenticate(true).token(token).build();
    }

    public IntrospectResponse Introspect(@NotNull IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        var verifier = new MACVerifier(SIGNER_KEY.getBytes());

        var jwtSigned = SignedJWT.parse(token);

        var verified = jwtSigned.verify(verifier);

        var expiredTime = jwtSigned.getJWTClaimsSet().getExpirationTime();

        return IntrospectResponse.builder()
                .valid(verified && expiredTime.after(new Date()))
                .build();
    }

    private String generateToken(@NotNull User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("dev")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(@NotNull User user) {
        var stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            //user.getRoles().forEach(stringJoiner::add);
        }

        return stringJoiner.toString();
    }
}
