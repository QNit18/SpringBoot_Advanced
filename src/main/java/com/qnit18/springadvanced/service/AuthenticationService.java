package com.qnit18.springadvanced.service;

import com.qnit18.springadvanced.dto.request.AuthenticationRequest;
import com.qnit18.springadvanced.dto.request.IntrospectRequest;
import com.qnit18.springadvanced.dto.request.LogoutRequest;
import com.qnit18.springadvanced.dto.request.RefreshRequest;
import com.qnit18.springadvanced.dto.response.AuthenticationResponse;
import com.qnit18.springadvanced.dto.response.IntrospectResponse;
import com.qnit18.springadvanced.entity.InvalidatedToken;
import com.qnit18.springadvanced.entity.User;
import com.qnit18.springadvanced.exception.AppException;
import com.qnit18.springadvanced.exception.ErrorCode;
import com.qnit18.springadvanced.repository.InvalidatedTokenRepository;
import com.qnit18.springadvanced.repository.UserRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;
        try{
            verifyToken(token);
        } catch (AppException e){
           isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Tạo token
        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(User user) {
        // Tạo header bằng HS512
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // Tạo data của payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("qnit18.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        // Tạo payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Đối tượng mang cả 2 giá trị
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            // Tạo signature
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Connot create token ", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user){
        // Vì scope trong config mặc định các role là chuỗi and mỗi role cách nhau " "
        StringJoiner stringJoiner = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission ->  stringJoiner.add(permission.getName()));
                }
            });
        }

        return stringJoiner.toString();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken((request.getToken()));

        String jit = signToken.getJWTClaimsSet().getJWTID();

        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expriryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken());

        var jit = signedJWT.getJWTClaimsSet().getJWTID();

        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expriryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );
        String token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (! (verified && expirationTime.after(new Date())) ){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }


        return signedJWT;
    }

}
