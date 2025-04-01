package com.example.readnovel.payload.response;

import com.example.readnovel.models.enumn.NovelStatusEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NovelListResponse {
    private Long id;
    private String title;
    private String description;
    private String slug;
    private String imageUrl;
    private Long recommendCount;
    private AuthorResponse author;
    private NovelStatusEnum status;
    private Double averageRating;
}
