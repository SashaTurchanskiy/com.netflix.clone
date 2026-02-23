package com.netflix.clone.service.impl;

import com.netflix.clone.dao.UserRepository;
import com.netflix.clone.dao.VideoRepository;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;
import com.netflix.clone.entity.User;
import com.netflix.clone.entity.Video;
import com.netflix.clone.service.WatchlistService;
import com.netflix.clone.util.PaginationUtils;
import com.netflix.clone.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public PageResponse<VideoResponse> getWatchlistPaginated(String email, int page, int size, String search) throws Exception {
            User user = serviceUtils.getUserByEmailOrThrow(email);

        Pageable pageable = PaginationUtils.createPageRequest(page, size);
        Page<Video> videoPage;

        if (search != null && !search.isEmpty()){
            videoPage = videoRepository.searchWatchlistByUserId(user.getId(), search.trim(), pageable);
        }else {
            videoPage = videoRepository.findWatchlistByUserId(user.getId(), pageable);
        }
        return PaginationUtils.toPageResponse(videoPage, VideoResponse::fromEntity);
    }
}
