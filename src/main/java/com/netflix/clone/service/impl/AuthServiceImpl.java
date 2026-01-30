package com.netflix.clone.service.impl;

import com.netflix.clone.dao.UserRepository;
import com.netflix.clone.dto.request.UserRequest;
import com.netflix.clone.dto.response.EmailValidationResponse;
import com.netflix.clone.dto.response.LoginResponse;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.entity.User;
import com.netflix.clone.enums.Role;
import com.netflix.clone.exception.*;
import com.netflix.clone.security.JwtUtil;
import com.netflix.clone.service.AuthService;
import com.netflix.clone.service.EmailService;
import com.netflix.clone.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final ServiceUtils serviceUtils;


    @Override
    public MessageResponse signup(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email is already in use: " + request.getEmail());
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(Role.USER);
        user.setActive(true);
        user.setEmailVerified(false);
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);
        return new MessageResponse("User registered successfully! Please check your email for verification.");
    }

    @Override
    public LoginResponse login(String email, String password) {
        User user = userRepository
                .findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(()-> new BadCredentialsException("Invalid email or password"));

        if (!user.isActive()){
            throw new AccountDeactivatedException("Account is deactivated. Please contact support.");
        }
        if (!user.isEmailVerified()){
            throw new BadCredentialsException("Email is not verified. Please verify your email before logging in.");
        }
        final String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponse(token, user.getEmail(), user.getFullName(), user.getRole().name());
    }

    @Override
    public EmailValidationResponse validateEmail(String email) {
        boolean exists = userRepository.existsByEmail(email);
        return new EmailValidationResponse(exists, !exists);
    }

    @Override
    public MessageResponse verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(()-> new InvalidTokenException("Invalid or expired verification token"));

        if (user.getVerificationTokenExpiry() == null || user.getVerificationTokenExpiry().isBefore(Instant.now())){
            throw new InvalidTokenException("Verification link has expired. Please request a new one");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        userRepository.save(user);
        return new MessageResponse("Email verified  successfully! u can login now");
    }

    @Override
    public MessageResponse resendVerification(String email) throws Exception {
        User user = serviceUtils.getUserByEmailOrThrow(email);

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(Instant.now().plusSeconds(86400));
        userRepository.save(user);
        emailService.sendVerificationEmail(email, verificationToken);
        return new MessageResponse("Verification email resent successfully. Please check your inbox");
    }

    @Override
    public MessageResponse forgotPassword(String email) throws Exception {
        User user = serviceUtils.getUserByEmailOrThrow(email);
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiry(Instant.now().plusSeconds(3600));
        userRepository.save(user);
        emailService.sendPasswordResetEmail(email, resetToken);

        return new MessageResponse("Password reset email sent successfully! Please check your inbox");
    }

    @Override
    public MessageResponse resetPassword(String token, String newPassword) {
        User user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(()-> new  InvalidTokenException("Invalid or expired reset token"));

        if (user.getPasswordResetToken() == null || user.getPasswordResetTokenExpiry().isBefore(Instant.now())){
            throw new InvalidTokenException("Reset token has expired");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        userRepository.save(user);

        return new MessageResponse("Password successfully reset. You can login with your new password now");
    }

    @Override
    public MessageResponse changePassword(String email, String currentPassword, String newPassword) throws Exception {
        User user = serviceUtils.getUserByEmailOrThrow(email);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())){
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return new MessageResponse("Password changed successfully");
    }

}
