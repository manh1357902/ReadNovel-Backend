package com.example.readnovel.payload.response;

import com.example.readnovel.models.enumn.NovelStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NovelResponse {
    private Long id;
    private String title;
    private String description;
    private String slug;
    private String imageUrl;
    private Long recommendCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponse createdBy;
    private UserResponse updatedBy;
    private AuthorResponse author;
    private NovelStatusEnum status;
    private Float averageRating;
    private Integer views;
    private Integer counterRating;
}
