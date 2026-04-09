package com.usermicro.userservicemicroservices.Exceptions;

import com.usermicro.userservicemicroservices.Payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handlerResourceNotFoundException(ResourceNotFoundException ex){
        String message = ex.getMessage();
        ApiResponse apiResponse = ApiResponse.builder().message(message).success(true).status(org.springframework.http.HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(apiResponse,org.springframework.http.HttpStatus.NOT_FOUND);
    }


}
