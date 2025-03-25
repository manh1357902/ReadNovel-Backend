package com.example.readnovel.service.impl;

import com.example.readnovel.models.entity.Novel;
import com.example.readnovel.payload.response.NovelResponse;
import com.example.readnovel.repository.NovelRepository;
import com.example.readnovel.service.NovelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NovelServiceImpl implements NovelService {

    private final NovelRepository novelRepository;
    @Override
    public List<Novel> getAllNovels() {
        return novelRepository.findAll();
    }
}
