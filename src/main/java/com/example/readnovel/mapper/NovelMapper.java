package com.example.readnovel.mapper;

import com.example.readnovel.models.entity.Novel;
import com.example.readnovel.payload.response.AuthorResponse;
import com.example.readnovel.payload.response.NovelListResponse;
import com.example.readnovel.payload.response.NovelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NovelMapper {

    private final UserMapper userMapper;

    public NovelResponse toNovelResponse(Novel novel) {
        return NovelResponse.builder()
                .id(novel.getId())
                .title(novel.getTitle())
                .description(novel.getDescription())
                .slug(novel.getSlug())
                .imageUrl(novel.getImageUrl())
                .recommendCount(novel.getRecommendCount())
                .status(novel.getStatus())
                .author(novel.getAuthor()!= null ? AuthorResponse.builder()
                        .id(novel.getAuthor().getId())
                        .fullName(novel.getAuthor().getFullName())
                        .build(): null)
                .createdAt(novel.getCreatedAt())
                .updatedAt(novel.getUpdatedAt())
                .createdBy(novel.getCreatedBy() != null ? userMapper.toUserResponse(novel.getCreatedBy()) : null)
                .updatedBy(novel.getUpdatedBy() != null ? userMapper.toUserResponse(novel.getUpdatedBy()) : null)
                .build();
    }
    public NovelListResponse toNovelListResponse(Novel novel) {
        return NovelListResponse.builder()
                .id(novel.getId())
                .title(novel.getTitle())
                .description(novel.getDescription())
                .slug(novel.getSlug())
                .imageUrl(novel.getImageUrl())
                .recommendCount(novel.getRecommendCount())
                .status(novel.getStatus())
                .author(novel.getAuthor() != null ? AuthorResponse.builder()
                        .id(novel.getAuthor().getId())
                        .fullName(novel.getAuthor().getFullName())
                        .build() : null)
                .build();
    }
}
