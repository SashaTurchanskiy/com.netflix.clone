package com.netflix.clone.service;

import com.netflix.clone.dto.request.UserRequest;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.UserResponse;
import org.jspecify.annotations.Nullable;

public interface UserService {
    MessageResponse createUser(UserRequest userRequest);

    MessageResponse updateUser(Long id, UserRequest userRequest) throws Exception;

    PageResponse<UserResponse> getUser(int page, int size, String search);

    MessageResponse deleteUser(Long id, String currentUserEmail) throws Exception;

    MessageResponse toggleUserStatus(Long id, String currentUserEmail) throws Exception;

    MessageResponse changeUserRole(Long id, UserRequest request) throws Exception;
}
