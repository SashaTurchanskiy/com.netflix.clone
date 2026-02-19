package com.netflix.clone.service;

import com.netflix.clone.dto.request.VideoRequest;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;
import com.netflix.clone.dto.response.VideoStatsResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface VideoService {
     MessageResponse createVideoByAdmin(VideoRequest request);

     PageResponse<VideoResponse> getAllAdminVideo(int page, int size, String search);

     MessageResponse updateVideoByAdmin(Long id, VideoRequest request);

     MessageResponse deleteVideoByAdmin(Long id);

     MessageResponse toggleVideoPublishStatusByAdmin(Long id, boolean value) throws Exception;

     VideoStatsResponse getAdminStats();

     PageResponse<VideoResponse> getPublishedVideo(int page, int size, String search, String email);

     List<VideoResponse> getFeaturedVideos();
}
