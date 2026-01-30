package com.netflix.clone.controller;

import com.netflix.clone.dto.request.*;
import com.netflix.clone.dto.response.EmailValidationResponse;
import com.netflix.clone.dto.response.LoginResponse;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate-email")
    public ResponseEntity<EmailValidationResponse> validateEmail(@RequestParam String email) {
        return ResponseEntity.ok(authService.validateEmail(email));
    }
    @GetMapping("/verify-email")
    public ResponseEntity<MessageResponse> verifyEmail(@RequestParam String token){
        return ResponseEntity.ok(authService.verifyEmail(token));
    }
    @PostMapping("/resendVerification")
    public ResponseEntity<MessageResponse> resendVerification(@Valid @RequestBody EmailRequest request) throws Exception {
        return ResponseEntity.ok(authService.resendVerification(request.getEmail()));
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody EmailRequest request) throws Exception {
        return ResponseEntity.ok(authService.forgotPassword(request.getEmail()));
    }
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        return ResponseEntity.ok(authService.resetPassword(request.getToken(), request.getNewPassword()));
    }
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication,
                                            @Valid @RequestBody ChangePasswordRequest request) throws Exception {
        String email = authentication.getName();
        return ResponseEntity.ok(authService.changePassword(email, request.getCurrentPassword(), request.getNewPassword()));
    }
}
