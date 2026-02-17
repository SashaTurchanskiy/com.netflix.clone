package com.netflix.clone.service;

import com.netflix.clone.dto.request.VideoRequest;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

public interface VideoService {
     MessageResponse createVideoByAdmin(VideoRequest request);

     PageResponse<VideoResponse> getAllAdminVideo(int page, int size, String search);
}
