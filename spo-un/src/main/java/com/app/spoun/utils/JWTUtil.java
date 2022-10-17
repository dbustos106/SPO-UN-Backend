package com.app.spoun.utils;

import com.app.spoun.security.ApplicationUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

public class JWTUtil {

    public static DecodedJWT verifyToken(String refresh_token, String SECRET){
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refresh_token);
        return decodedJWT;
    }

    public static String createToken(ApplicationUser user, String SECRET, String ISSUER, int EXPIRES_IN){

        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        String subject = user.getUsername() + "," + user.getId();

        String token = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRES_IN))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer(ISSUER)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        return token;
    }

}
