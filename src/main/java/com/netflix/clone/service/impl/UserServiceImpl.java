package com.netflix.clone.service.impl;

import com.netflix.clone.dao.UserRepository;
import com.netflix.clone.dto.request.UserRequest;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.entity.User;
import com.netflix.clone.enums.Role;
import com.netflix.clone.exception.EmailAlreadyExistsException;
import com.netflix.clone.exception.InvalidRoleException;
import com.netflix.clone.service.EmailService;
import com.netflix.clone.service.UserService;
import com.netflix.clone.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceUtils serviceUtils;
    private final EmailService emailService;

    @Override
    public MessageResponse createUser(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        validateRole(userRequest.getRole());

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setFullName(userRequest.getFullName());
        user.setRole(Role.valueOf(userRequest.getRole().toUpperCase()));
        user.setActive(true);
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setPasswordResetTokenExpiry(Instant.now().plusSeconds(86400));
        userRepository.save(user);
        emailService.sendVerificationEmail(userRequest.getEmail(), verificationToken);

        return new MessageResponse("User created successfully");
    }

    private void validateRole(String role) {
        if (Arrays.stream(Role.values()).noneMatch(r->r.name().equals(role))){
            throw new InvalidRoleException("Invalid role: " + role);
        }
    }
}
