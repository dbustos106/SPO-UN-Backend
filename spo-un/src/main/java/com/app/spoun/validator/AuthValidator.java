package com.app.spoun.validator;

import com.app.spoun.exceptions.Apiunauthorized;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

@Component
public class AuthValidator {

    private static final String CLIENT_CREDENTIALS = "client_credentials";

    public void validate(MultiValueMap<String, String> paramMap, String grantType) throws Apiunauthorized {
        if(grantType.isEmpty() || !grantType.equals(CLIENT_CREDENTIALS)){
            message("The grantType field is invalid");
        }

        if(Objects.isNull(paramMap) ||
                paramMap.getFirst("client_id").isEmpty() ||
                paramMap.getFirst("client_secret").isEmpty()){
            message("client_id and/or client_secret are invalid");
        }
    }

    private void message(String message) throws Apiunauthorized{
        throw new Apiunauthorized(message);
    }

}
