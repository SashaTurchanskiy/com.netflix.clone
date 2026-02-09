package com.netflix.clone.service;

import com.netflix.clone.dto.request.UserRequest;
import com.netflix.clone.dto.response.MessageResponse;
import org.jspecify.annotations.Nullable;

public interface UserService {
    MessageResponse createUser(UserRequest userRequest);
}
