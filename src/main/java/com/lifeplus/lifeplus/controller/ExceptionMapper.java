package com.lifeplus.lifeplus.controller;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Manuel Pedrozo
 */
@RestControllerAdvice
public class ExceptionMapper extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleException(DataIntegrityViolationException ex, WebRequest request) {
        final Map<String, Object> errorAttributes = new DefaultErrorAttributes().getErrorAttributes(request, false);
        errorAttributes.put("status", HttpStatus.CONFLICT.value());
        errorAttributes.put("message", "Data integrity violation");
        errorAttributes.put("error", HttpStatus.CONFLICT.getReasonPhrase());
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    protected ResponseEntity<Object> handleException(BadCredentialsException ex, WebRequest request) {
        final Map<String, Object> errorAttributes = new DefaultErrorAttributes().getErrorAttributes(request, false);
        errorAttributes.put("status", HttpStatus.UNAUTHORIZED.value());
        errorAttributes.put("message", ex.getMessage());
        errorAttributes.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    protected ResponseEntity<Object> handleException(NoSuchElementException ex, WebRequest request) {
        final Map<String, Object> errorAttributes = new DefaultErrorAttributes().getErrorAttributes(request, false);
        errorAttributes.put("status", HttpStatus.NOT_FOUND.value());
        errorAttributes.put("message", ex.getMessage());
        errorAttributes.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        return handleExceptionInternal(ex, errorAttributes, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

}
