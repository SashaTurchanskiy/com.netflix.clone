package com.netflix.clone.controller;

import com.netflix.clone.dto.request.UserRequest;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.UserResponse;
import com.netflix.clone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/update/{id}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) throws Exception {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }
    @GetMapping()
    public ResponseEntity<PageResponse<UserResponse>> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search){

        return ResponseEntity.ok(userService.getUser(page, size, search));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id, Authentication authentication) throws Exception {
        String currentUserEmail = authentication.getName();
        return ResponseEntity.ok(userService.deleteUser(id, currentUserEmail));
    }
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<MessageResponse> toggleUserStatus(
            @PathVariable Long id, Authentication authentication) throws Exception {
        String currentUserEmail = authentication.getName();
        return ResponseEntity.ok(userService.toggleUserStatus(id, currentUserEmail));
    }
    @PutMapping("/{id}/change-role")
    public ResponseEntity<MessageResponse> changeUserRole(
            @PathVariable Long id, @RequestBody UserRequest request) throws Exception {
     return ResponseEntity.ok(userService.changeUserRole(id, request));
    }

}
