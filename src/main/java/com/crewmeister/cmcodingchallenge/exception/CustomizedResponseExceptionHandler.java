package com.crewmeister.cmcodingchallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<Object> handlerExceptions(BusinessException ex, WebRequest request){

        ExceptionResponse response =
                new ExceptionResponse(new Date(),ex.getMessage(), request.getDescription(false),
                ex.getHttpStatus()==null? HttpStatus.BAD_REQUEST:ex.getHttpStatus()
             );
        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
