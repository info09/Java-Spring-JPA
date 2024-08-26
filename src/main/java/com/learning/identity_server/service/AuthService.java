package com.learning.identity_server.service;

import com.learning.identity_server.dto.request.AuthRequest;
import com.learning.identity_server.dto.request.IntrospectRequest;
import com.learning.identity_server.dto.request.LogoutRequest;
import com.learning.identity_server.dto.response.AuthResponse;
import com.learning.identity_server.dto.response.IntrospectResponse;
import com.learning.identity_server.entity.InvalidatedToken;
import com.learning.identity_server.entity.User;
import com.learning.identity_server.exception.AppException;
import com.learning.identity_server.exception.ErrorCode;
import com.learning.identity_server.repository.IInvalidatedTokenRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    IUserRepository _userRepository;
    IInvalidatedTokenRepository _invalidatedTokenRepository;
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
        var isValid = true;

        try {
            verifyToken(token);
        } catch (AppException ex) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        var jwtId = signToken.getJWTClaimsSet().getJWTID();
        var expirationTime = signToken.getJWTClaimsSet().getExpirationTime();

        var invalidateToken = InvalidatedToken.builder().id(jwtId).expiredTime(expirationTime).build();

        _invalidatedTokenRepository.save(invalidateToken);
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        var verifier = new MACVerifier(SIGNER_KEY.getBytes());
        var jwtSigned = SignedJWT.parse(token);

        var verified = jwtSigned.verify(verifier);

        var expiredTime = jwtSigned.getJWTClaimsSet().getExpirationTime();

        if (!(verified && expiredTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (_invalidatedTokenRepository
                .existsById(jwtSigned.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return jwtSigned;
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
                .jwtID(UUID.randomUUID().toString())
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
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission ->
                            stringJoiner.add(permission.getName())
                    );
                }
            });
        }

        return stringJoiner.toString();
    }
}
