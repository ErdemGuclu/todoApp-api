package com.erdemkara.todoapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ExistingDataException extends RuntimeException{
    public ExistingDataException(String message)
    {
        super(message);
    }
}
