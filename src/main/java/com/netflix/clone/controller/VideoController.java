package com.netflix.clone.controller;

import com.netflix.clone.dto.request.VideoRequest;
import com.netflix.clone.dto.response.MessageResponse;
import com.netflix.clone.dto.response.PageResponse;
import com.netflix.clone.dto.response.VideoResponse;
import com.netflix.clone.dto.response.VideoStatsResponse;
import com.netflix.clone.entity.Video;
import com.netflix.clone.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<MessageResponse> createVideoByAdmin(@Valid @RequestBody VideoRequest request){
        return ResponseEntity.ok(videoService.createVideoByAdmin(request));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<PageResponse<VideoResponse>> getAllAdminVideo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search){
        return ResponseEntity.ok(videoService.getAllAdminVideo(page, size, search));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<MessageResponse> updateVideoByAdmin(@PathVariable Long id, @Valid @RequestBody VideoRequest request) {
        // Implement the update logic in the service layer
        return ResponseEntity.ok(videoService.updateVideoByAdmin(id, request));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<MessageResponse> deleteVideoByAdmin(@PathVariable Long id) {
        // Implement the delete logic in the service layer
        return ResponseEntity.ok(videoService.deleteVideoByAdmin(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/{id}/publish")
    public ResponseEntity<MessageResponse> toggleVideoPublishStatusByAdmin(@PathVariable Long id, @RequestParam boolean value) throws Exception {
        return ResponseEntity.ok(videoService.toggleVideoPublishStatusByAdmin(id, value));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/stats")
    public ResponseEntity<VideoStatsResponse> getAdminStats(){
        return ResponseEntity.ok(videoService.getAdminStats());
    }
    @GetMapping("/published")
    public ResponseEntity<PageResponse<VideoResponse>> getPublishedVideo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Authentication authentication){

        String email = authentication.getName();
        PageResponse<VideoResponse> response = videoService.getPublishedVideo(page, size, search, email);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/featured")
    public ResponseEntity<List<VideoResponse>> getFeaturedVideos(){
        List<VideoResponse> featuredVideos = videoService.getFeaturedVideos();
        return ResponseEntity.ok(featuredVideos);
    }
}
