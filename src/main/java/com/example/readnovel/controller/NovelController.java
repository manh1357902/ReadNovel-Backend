package com.example.readnovel.controller;

import com.example.readnovel.models.entity.Novel;
import com.example.readnovel.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Book;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class NovelController {

    private final NovelService novelService;
     @GetMapping("user/novels")
     public ResponseEntity<List<Novel>> getAllBooks() {
         List<Novel> novels = novelService.getAllNovels();
         return ResponseEntity.ok(novels);
     }
}
