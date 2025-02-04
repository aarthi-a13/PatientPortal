package com.vp.poc.patientportal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParsedDataException extends RuntimeException {
    public ParsedDataException(String message) {
        super(message);
    }
}