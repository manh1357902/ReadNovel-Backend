package com.example.readnovel.service.impl;

import com.example.readnovel.constrains.Message;
import com.example.readnovel.mapper.NovelMapper;
import com.example.readnovel.models.entity.*;
import com.example.readnovel.payload.request.FilterNovelNewsRequest;
import com.example.readnovel.payload.request.NovelCreateRequest;
import com.example.readnovel.payload.request.NovelUpdateRequest;
import com.example.readnovel.payload.response.ApiResponse;
import com.example.readnovel.payload.response.CustomPageResponse;
import com.example.readnovel.payload.response.ErrorResponse;
import com.example.readnovel.payload.response.NovelListResponse;
import com.example.readnovel.repository.AuthorRepository;
import com.example.readnovel.repository.CategoryRepository;
import com.example.readnovel.repository.NovelRepository;
import com.example.readnovel.repository.RatingRepository;
import com.example.readnovel.security.CustomUserDetails;
import com.example.readnovel.service.ImageService;
import com.example.readnovel.service.NovelService;
import com.example.readnovel.utils.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class NovelServiceImpl implements NovelService {

    private final NovelRepository novelRepository;
    private final SlugUtils slugUtils;
    private final ImageService imageService;
    private final AuthorRepository authorRepository;
    private final NovelMapper novelMapper;
    private final CategoryRepository categoryRepository;
    private final RatingRepository ratingRepository;

    @Override
    public ResponseEntity<Object> getNovelNewUpdate(int page, int size) {
        Page<Novel> novels = novelRepository.getAllByIsDeletedFalse(PageRequest.of(page - 1, size));
        return entityToResponse(novels);
    }

    @Override
    public ResponseEntity<Object> getNovelFilter(FilterNovelNewsRequest request) {
        Page<Novel> novels = novelRepository.getNovelsFilter(request.getTitle(), request.getStatus(), request.getCategoryIds(), request.getMinChapter(), request.getMaxChapter(), PageRequest.of(request.getPage() - 1, request.getSize(),Sort.by(request.getSort()).descending()));
        return entityToResponse(novels);
    }

    private ResponseEntity<Object> entityToResponse(Page<Novel> novels) {
        Page<NovelListResponse> novelResponses = novels.map(novel -> {
            NovelListResponse novelResponse = novelMapper.toNovelListResponse(novel);
            novelResponse.setAverageRating(calculateAverageRating(novel.getId()).get("averageRating"));
            return novelResponse;
        });
        return ResponseEntity.ok(ApiResponse.builder().code(HttpStatus.OK.value()).message(Message.SUCCESS).data(new CustomPageResponse<>(novelResponses)).build());
    }

    @Override
    @Transactional
    public ResponseEntity<Object> createdNovel(NovelCreateRequest novelRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        Optional<Author> author = authorRepository.findByFullName(novelRequest.getAuthor());
        Author authorNew;
        authorNew = author.orElseGet(() -> Author.builder()
                .fullName(novelRequest.getAuthor())
                .build());
        List<Category> categories = categoryRepository.findAllById(novelRequest.getCategoryIds());
        try{
            String imageUrl = imageService.uploadImage(novelRequest.getImage());
            Novel novel = Novel.builder()
                    .title(novelRequest.getTitle())
                    .description(novelRequest.getDescription())
                    .slug(slugUtils.generateSlug(novelRequest.getTitle()))
                    .imageUrl(imageUrl)
                    .author(authorNew)
                    .createdBy(user)
                    .categories(categories)
                    .status(novelRequest.getStatus())
                    .isDeleted(false)
                    .build();
            Novel novelSaved = novelRepository.save(novel);
            return ResponseEntity.ok(ApiResponse.builder().code(HttpStatus.CREATED.value()).message(Message.SUCCESS).data(novelMapper.toNovelResponse(novelSaved)).build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ErrorResponse.builder().code(HttpStatus.BAD_REQUEST.value()).message(e.getMessage()).build());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> updateNovel(Long id, NovelUpdateRequest novelRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
            Novel novel = novelRepository.findById(id).orElseThrow(() -> new RuntimeException(Message.NOVEL_NOT_FOUND));
            Optional<Author> author = authorRepository.findByFullName(novelRequest.getAuthor());
            Author authorNew;
            authorNew = author.orElseGet(() -> Author.builder()
                    .fullName(novelRequest.getAuthor())
                    .build());
            List<Category> categories = categoryRepository.findAllById(novelRequest.getCategoryIds());
            novel.setTitle(novelRequest.getTitle());
            novel.setDescription(novelRequest.getDescription());
            novel.setSlug(slugUtils.generateSlug(novelRequest.getTitle()));
            novel.setAuthor(authorNew);
            novel.setUpdatedBy(user);
            novel.setCategories(categories);
            if(novelRequest.getImage()!=null){
                String imageUrl = imageService.uploadImage(novelRequest.getImage());
                novel.setImageUrl(imageUrl);
                imageService.deleteImage(novel.getImageUrl());
            }
            Novel novelSaved = novelRepository.save(novel);
            return ResponseEntity.ok(ApiResponse.builder().code(HttpStatus.OK.value()).message(Message.SUCCESS).data(novelMapper.toNovelResponse(novelSaved)).build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ErrorResponse.builder().code(HttpStatus.BAD_REQUEST.value()).message(e.getMessage()).build());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Object> deleteNovel(Long id) {
        try{
            Novel novel = novelRepository.findById(id).orElseThrow(() -> new RuntimeException(Message.NOVEL_NOT_FOUND));
            novel.setIsDeleted(true);
            return ResponseEntity.ok(ApiResponse.builder().code(HttpStatus.OK.value()).message(Message.SUCCESS).build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ErrorResponse.builder().code(HttpStatus.BAD_REQUEST.value()).message(e.getMessage()).build());
        }
    }

    private Map<String, Double> calculateAverageRating(Long novelId) {
        List<Rating> ratings = Optional.ofNullable(ratingRepository.findAllByNovelId(novelId)).orElse(Collections.emptyList());
        double avgRating = ratings.stream().mapToDouble(Rating::getStar).average().orElse(0.0);
        double counter = ratings.size();
        return Map.of("averageRating", avgRating, "counterRating", counter);
    }

}
