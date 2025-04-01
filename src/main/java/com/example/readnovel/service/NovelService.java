package com.example.readnovel.service;

import com.example.readnovel.payload.request.FilterNovelNewsRequest;
import com.example.readnovel.payload.request.NovelCreateRequest;
import com.example.readnovel.payload.request.NovelUpdateRequest;
import org.springframework.http.ResponseEntity;

public interface NovelService {
    ResponseEntity<Object> getNovelNewUpdate(int page, int size);
    ResponseEntity<Object> getNovelFilter(FilterNovelNewsRequest request);
    ResponseEntity<Object> createdNovel(NovelCreateRequest novelRequest);
    ResponseEntity<Object> updateNovel(Long id, NovelUpdateRequest novelRequest);
    ResponseEntity<Object> deleteNovel(Long id);
}
