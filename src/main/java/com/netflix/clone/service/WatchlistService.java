package com.netflix.clone.service;

import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;
import org.jspecify.annotations.Nullable;

public interface WatchlistService {

    MessageResponse addToWatchlist(String email, Long videoId) throws Exception;

    MessageResponse removeFromWatchlist(String email, Long videoId) throws Exception;

    PageResponse<VideoResponse> getWatchlistPaginated(String email, int page, int size, String search) throws Exception;
}
