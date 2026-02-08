package com.netflix.clone.service;

import com.netflix.clone.dto.request.EmailRequest;
import com.netflix.clone.dto.request.UserRequest;
import com.netflix.clone.dto.response.EmailValidationResponse;
import com.netflix.clone.dto.response.LoginResponse;
import com.netflix.clone.dto.response.MessageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

public interface AuthService {
    MessageResponse signup(UserRequest request);
    LoginResponse login(String email, String password);

    EmailValidationResponse validateEmail(String email);

    MessageResponse verifyEmail(String token);

    MessageResponse resendVerification(String email) throws Exception;

    MessageResponse forgotPassword(String email) throws Exception;

    MessageResponse resetPassword(String token,String newPassword);

    MessageResponse changePassword(String email, String currentPassword,  String newPassword) throws Exception;

    LoginResponse currentUser(String email) throws Exception;
}
