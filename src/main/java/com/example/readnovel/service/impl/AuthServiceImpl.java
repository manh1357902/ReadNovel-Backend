package com.example.readnovel.service.impl;

import com.example.readnovel.constrains.Message;
import com.example.readnovel.jwt.JwtUtils;
import com.example.readnovel.mapper.UserMapper;
import com.example.readnovel.models.entity.Role;
import com.example.readnovel.models.entity.User;
import com.example.readnovel.payload.request.LoginRequest;
import com.example.readnovel.payload.request.RefreshTokenRequest;
import com.example.readnovel.payload.request.RegisterRequest;
import com.example.readnovel.payload.response.ApiResponse;
import com.example.readnovel.payload.response.ErrorResponse;
import com.example.readnovel.payload.response.LoginResponse;
import com.example.readnovel.payload.response.UserResponse;
import com.example.readnovel.repository.RoleRepository;
import com.example.readnovel.repository.UserRepository;
import com.example.readnovel.security.CustomUserDetails;
import com.example.readnovel.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public ResponseEntity<Object> login(LoginRequest loginRequest) {
        if(!userRepository.existsByEmail(loginRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(Message.EMAIL_NOT_FOUND)
                    .build());
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
            String accessToken = jwtUtils.generateAccessToken(user.getEmail());
            String refreshToken = jwtUtils.generateRefreshToken(user.getEmail(), null);
            UserResponse userResponse = userMapper.toUserResponse(user);
            LoginResponse loginResponse = LoginResponse.builder()
                    .user(userResponse)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            return ResponseEntity.ok(ApiResponse.builder().code(HttpStatus.OK.value()).message(Message.SUCCESS).data(loginResponse).build());
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> register(RegisterRequest registerRequest) {
        try {
            Role role = roleRepository.findByRoleName("USER").orElseThrow(() -> new RuntimeException("Role not found"));
            if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(ErrorResponse.builder().code(HttpStatus.BAD_REQUEST.value())
                        .message(Message.CONFIRM_PASSWORD_NOT_CORRECT)
                        .build());
            }
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest().body(ErrorResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(Message.EMAIL_ALREADY_EXISTS)
                        .build());
            }
            Random random = new Random();
            StringBuilder fullName = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                fullName.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
            }
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            User user = User.builder()
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .fullName(fullName.toString())
                    .roles(roles)
                    .build();
            userRepository.save(user);
            return new ResponseEntity<>(ApiResponse.builder()
                    .code(HttpStatus.CREATED.value())
                    .message(Message.SUCCESS)
                    .build(), HttpStatus.CREATED);
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @Override
    public ResponseEntity<Object> refresh(RefreshTokenRequest tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken();
        if (!jwtUtils.validateJwtToken(refreshToken, true)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = jwtUtils.getEmailFromToken(refreshToken, true);
        List<String> storedTokens = (List<String>) redisTemplate.opsForValue().get("black_list_refresh_token_"+ email);
        Date expirationDateRefreshToken = jwtUtils.getExpirationDateFromToken(refreshToken,true);
        if (storedTokens == null || storedTokens.contains(tokenRequest.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message(Message.FAILED)
                    .build());
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(Message.EMAIL_NOT_FOUND));
        String newAccessToken = jwtUtils.generateAccessToken(user.getEmail());
        String newRefreshToken = jwtUtils.generateRefreshToken(user.getEmail(), expirationDateRefreshToken);
        UserResponse userResponse = userMapper.toUserResponse(user);
        LoginResponse loginResponse = LoginResponse.builder()
                .user(userResponse)
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
        return ResponseEntity.ok(loginResponse);
    }
}
