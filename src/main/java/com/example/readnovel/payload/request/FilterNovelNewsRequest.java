package com.example.readnovel.payload.request;

import com.example.readnovel.models.enumn.NovelStatusEnum;
import lombok.Data;

import java.util.List;

@Data
public class FilterNovelNewsRequest {
    private String title;
    private Integer page = 1;
    private Integer size = 20;
    private String sort = "createdAt";
    private NovelStatusEnum status;
    private List<Integer> categoryIds;
    private Integer maxChapter;
    private Integer minChapter;
    private Integer recommend;
}
