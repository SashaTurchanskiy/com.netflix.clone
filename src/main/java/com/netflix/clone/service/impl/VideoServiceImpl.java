package com.netflix.clone.service.impl;

import com.netflix.clone.dao.UserRepository;
import com.netflix.clone.dao.VideoRepository;
import com.netflix.clone.dto.request.VideoRequest;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;
import com.netflix.clone.entity.Video;
import com.netflix.clone.service.VideoService;
import com.netflix.clone.util.PaginationUtils;
import com.netflix.clone.util.ServiceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final ServiceUtils serviceUtils;

    @Override
    public MessageResponse createVideoByAdmin(VideoRequest request) {
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setYear(request.getYear());
        video.setRating(request.getRating());
        video.setDuration(request.getDuration());
        video.setSrcUuid(request.getSrc());
        video.setPosterUuid(request.getPoster());
        video.setPublished(request.isPublished());
        video.setCategories(request.getCategories() != null ? request.getCategories() : List.of());

        videoRepository.save(video);
        return new MessageResponse("Video created successfully");
    }

    @Override
    public PageResponse<VideoResponse> getAllAdminVideo(int page, int size, String search) {
        Pageable pageable = PaginationUtils.createPageRequest(page, size, "id");
        Page<Video> videoPage;

        if (search != null && !search.trim().isEmpty()){
            videoPage = videoRepository.searchVideos(search.trim(), pageable);
        } else {
            videoPage = videoRepository.findAll(pageable);
        }
        return PaginationUtils.toPageResponse(videoPage, VideoResponse::fromEntity);
    }

    @Override
    public MessageResponse updateVideoByAdmin(Long id, VideoRequest request) {
        Video video = new Video();
        video.setId(id);
        video.setTitle(request.getTitle());
        video.setDescription(request.getDescription());
        video.setYear(request.getYear());
        video.setRating(request.getRating());
        video.setDuration(request.getDuration());
        video.setSrcUuid(request.getSrc());
        video.setPosterUuid(request.getPoster());
        video.setCategories(request.getCategories() != null ? request.getCategories() : List.of());

        videoRepository.save(video);
        return new MessageResponse("Video updated successfully");

    }

    @Override
    public MessageResponse deleteVideoByAdmin(Long id) {
        if (!videoRepository.existsById(id)){
            throw new IllegalArgumentException("Video not found with id: " + id);
        }
        videoRepository.deleteById(id);
        return new MessageResponse("Video deleted successfully");
    }


}
