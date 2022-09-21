package com.app.spoun.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class Apiunauthorized extends Exception{

    public Apiunauthorized(String messsage){
        super(messsage);
    }

}
