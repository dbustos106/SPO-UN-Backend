package com.app.spoun.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtIOComponent {

    public static String ISSUER;
    public static String SECRET;

    public static int ACCESS_EXPIRES_IN;
    public static int REFRESH_EXPIRES_IN;

    @Value("${jms.jwt.issuer:none}")
    public void setISSUER(String issuer) {
        ISSUER = issuer;
    }

    @Value("${jms.jwt.token.secret:secret}")
    public void setSECRET(String secret) {
        SECRET = secret;
    }

    @Value("${jms.jwt.token.access_expires_in:600000}")
    public void setAccessExpiresIn(int accessExpiresIn) {
        ACCESS_EXPIRES_IN = accessExpiresIn;
    }

    @Value("${jms.jwt.token.refresh_expires_in:120000}")
    public void setRefreshExpiresIn(int refreshExpiresIn) {
        REFRESH_EXPIRES_IN = refreshExpiresIn;
    }

}
