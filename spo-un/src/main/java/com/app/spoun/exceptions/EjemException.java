package com.app.spoun.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EjemException extends Exception{

    public EjemException(String message){
        super(message);
    }

}
