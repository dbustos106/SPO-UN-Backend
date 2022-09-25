package com.app.spoun.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.stream.Collectors;

public class JWTUtil {

    public static DecodedJWT verifyToken(String refresh_token, String SECRET){
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refresh_token);
        return decodedJWT;
    }

    public static String createToken(UserDetails user, String SECRET, String ISSUER, int EXPIRES_IN){

        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        //String subject = GsonUtil.serialize(user.getUsername());

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRES_IN))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer(ISSUER)
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        return token;
    }

}
