package com.example.readnovel.payload.response;

import com.example.readnovel.models.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private UserResponse user;
    private String accessToken;
    private String refreshToken;
}
