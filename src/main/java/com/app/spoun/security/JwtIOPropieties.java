package com.app.spoun.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jms.jwt")
public class JwtIOPropieties {

    private String timezone;
    private String issuer;
    private Token token;
    private Excluded excluted;

    public JwtIOPropieties(){
    }

    @Data
    public static class Token{
        private Auth auth;
        private String secret;
        private int access_expires_in;
        private int refresh_expires_in;
    }

    @Data
    public static class Auth{
        private String path;
    }

    @Data
    public static class Excluded{
        private String path;
    }

}
