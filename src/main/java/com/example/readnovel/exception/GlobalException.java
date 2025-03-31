package com.example.readnovel.exception;

import com.example.readnovel.constrains.Message;
import com.example.readnovel.payload.response.ApiResponse;
import com.example.readnovel.payload.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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
    // Xử lý lỗi quá kích thước
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(ErrorResponse.builder().code(HttpStatus.PAYLOAD_TOO_LARGE.value()).message(ex.getMessage()).build());
    }

    // Bắt lỗi đường dẫn API sai
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().code(HttpStatus.NOT_FOUND.value()).message(ex.getMessage()).build());
    }
}
