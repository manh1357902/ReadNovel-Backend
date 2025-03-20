package com.example.readnovel.exception;

import com.example.readnovel.constrains.Message;
import com.example.readnovel.payload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler
    public Map<String, String> exceptionHandler(Exception exception){
        Map<String, String> map = new HashMap<>();
        map.put("message", exception.getMessage());
        return map;
    }

    @ExceptionHandler
    public ResponseEntity<Object> validationException(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(ApiResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(Message.FAILED)
                .data(errors).build()
                , HttpStatus.BAD_REQUEST);
    }
}
