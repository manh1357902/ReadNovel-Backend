package com.example.readnovel.payload.request;

import com.example.readnovel.constrains.Message;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = Message.EMAIL_NOT_EMPTY)
    private String email;
    @NotBlank(message = Message.PASSWORD_NOT_EMPTY)
    private String password;
}
