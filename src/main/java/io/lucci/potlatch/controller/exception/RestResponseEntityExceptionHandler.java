package io.lucci.potlatch.controller.exception;

import java.lang.reflect.UndeclaredThrowableException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import io.lucci.potlatch.model.Error;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger( RestResponseEntityExceptionHandler.class );

    
    @ResponseStatus (HttpStatus.NOT_FOUND)
    @ExceptionHandler (value = { ResourceNotFoundException.class })
    protected @ResponseBody Error handleAccessDeniedException( Exception ex, WebRequest request ) {
        return new Error( ErrorCode.RESOURCE_NOT_FOUND, ex.getMessage() );
    }


    @ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler (value = { Exception.class, UndeclaredThrowableException.class })
    protected @ResponseBody Error handleConflict( Exception ex, WebRequest request ) {
        return new Error( ErrorCode.GENERIC_ERROR, ex.getMessage() );
    }

    

}