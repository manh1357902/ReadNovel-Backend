package com.example.readnovel.controller;

import com.example.readnovel.payload.request.FilterNovelNewsRequest;
import com.example.readnovel.payload.request.NovelCreateRequest;
import com.example.readnovel.payload.request.NovelUpdateRequest;
import com.example.readnovel.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/novels/")
public class NovelController {

    private final NovelService novelService;

    @GetMapping("filter")
    public ResponseEntity<Object> getNovelFilter(@RequestBody FilterNovelNewsRequest request) {
        return novelService.getNovelFilter(request);
    }

    @GetMapping("new-chapter")
    public ResponseEntity<Object> getNovelNewUpdate(@RequestParam(name = "page", defaultValue = "1") int page,
                                                    @RequestParam(name = "size", defaultValue = "20") int size) {
        return novelService.getNovelNewUpdate(page - 1, size);
    }

    @PostMapping("create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> createNovel(@ModelAttribute NovelCreateRequest novelRequest) {
        return novelService.createdNovel(novelRequest);
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> updateNovel(@PathVariable(name = "id") Long id, @ModelAttribute NovelUpdateRequest novelRequest) {
        return novelService.updateNovel(id, novelRequest);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> deleteNovel(@PathVariable(name = "id") Long id) {
        return novelService.deleteNovel(id);
    }
}
