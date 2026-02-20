package com.netflix.clone.service.impl;

import com.netflix.clone.dao.UserRepository;
import com.netflix.clone.dao.VideoRepository;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.entity.User;
import com.netflix.clone.entity.Video;
import com.netflix.clone.service.WatchlistService;
import com.netflix.clone.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WatchlistServiceImpl implements WatchlistService {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final ServiceUtils serviceUtils;

    @Override
    public MessageResponse addToWatchlist(String email, Long videoId) throws Exception {
        User user = serviceUtils.getUserByEmailOrThrow(email);

        Video video = serviceUtils.getVideoByIdOrThrow(videoId);

        user.addToWatchlist(video);
        userRepository.save(user);

        return new MessageResponse("Video added to watchlist successfully");
    }

    @Override
    public MessageResponse removeFromWatchlist(String email, Long videoId) throws Exception {
        User user = serviceUtils.getUserByEmailOrThrow(email);

        Video video = serviceUtils.getVideoByIdOrThrow(videoId);

        user.removeFromWatchlist(video);
        userRepository.save(user);
        return new MessageResponse("Video removed from watchlist successfully");
    }
}
