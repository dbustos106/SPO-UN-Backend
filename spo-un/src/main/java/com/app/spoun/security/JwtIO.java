package com.app.spoun.security;

import com.app.spoun.dto.UserDetails;
import com.app.spoun.utils.GsonUtil;
import io.fusionauth.jwt.JWTUtils;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;

@Component
public class JwtIO {

    @Value("${jms.jwt.token.secret:secret}")
    private String SECRET;
    @Value("${jms.jwt.timezone:UTC}")
    private String TIMEZONE;
    @Value("${jms.jwt.token.expiresIn:3600}")
    private int EXPIRES_IN;
    @Value("${jms.jwt.issuer:none}")
    private String ISSUER;

    public String generateToken(Object src){

        String subject = GsonUtil.serialize(src);

        // Build an HMAC signer to encrypt using SHA-256
        Signer signer = HMACSigner.newSHA256Signer(SECRET);

        TimeZone tz = TimeZone.getTimeZone(TIMEZONE);
        ZonedDateTime zdt = ZonedDateTime.now(tz.toZoneId()).plusSeconds(EXPIRES_IN);

        JWT jwt = new JWT()
                .setIssuer(ISSUER)
                .setIssuedAt(ZonedDateTime.now(tz.toZoneId()))
                .setSubject(subject) // Payload
                .setExpiration(zdt);

        return JWT.getEncoder().encode(jwt, signer);
    }

    public boolean validateToken(String encodedJWT){
        boolean result = false;
        try {
            JWT jwt = jwt(encodedJWT);
            result = !jwt.isExpired();
        }catch (Exception e){
            System.out.println(e);
        }

        return result;
    }

    public String extractUsername(String encodedJWT) {
        UserDetails userDetails = getPayload(encodedJWT);
        return userDetails.getUsername();
    }

    public String extractRole(String encodedJWT) {
        UserDetails userDetails = getPayload(encodedJWT);
        return userDetails.getRole();
    }

    public UserDetails getPayload(String encodedJWT){
        Map<String, Object> claims = jwt(encodedJWT).getAllClaims();
        UserDetails userDetails = GsonUtil.toObject(claims.get("sub").toString(), new UserDetails().getClass());
        return userDetails;
    }

    private JWT jwt(String encodedJWT){
        JWT jwt = JWTUtils.decodePayload(encodedJWT);
        return jwt;
    }

}
