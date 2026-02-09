package com.netflix.clone.controller;

import com.netflix.clone.dto.request.UserRequest;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponse> createUser(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.createUser(userRequest));
    }
}
