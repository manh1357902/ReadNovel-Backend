package com.example.readnovel.payload.request;

import com.example.readnovel.constrains.Message;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = Message.EMAIL_NOT_EMPTY)
    @Email(message = Message.EMAIL_NOT_FORMAT)
    private String email;
    @NotBlank(message = Message.PASSWORD_NOT_EMPTY)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$",
            message = Message.PASSWORD_NOT_CORRECT_FORMAT
    )
    private String password;
    @NotBlank(message = Message.CONFIRM_EMAIL_NOT_EMPTY)
    private String confirmPassword;
}
