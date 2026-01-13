package com.netflix.clone.util;

import com.netflix.clone.dao.UserRepository;
import com.netflix.clone.dao.VideoRepository;
import com.netflix.clone.entity.User;
import com.netflix.clone.entity.Video;
import com.netflix.clone.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceUtils {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    public User getUserByEmailOrThrow(String email) throws Exception {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with email: " + email));
    }
    public User getUserByIdOrThrow(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + id));
    }
    public Video getVideoByIdOrThrow(Long id) throws Exception {
        return videoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Video not found with id: " + id));
    }
}
