package com.netflix.clone.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoStatsResponse {

    private Long totalVideos;
    private Long publishedVideos;
    private Long totalDuration;

}
