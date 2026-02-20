package com.netflix.clone.service;

import com.netflix.clone.dto.response.MessageResponse;
import org.jspecify.annotations.Nullable;

public interface WatchlistService {

    MessageResponse addToWatchlist(String email, Long videoId) throws Exception;

    MessageResponse removeFromWatchlist(String email, Long videoId) throws Exception;
}
