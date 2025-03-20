package com.example.readnovel.service;

import com.example.readnovel.payload.response.NovelResponse;

import java.util.List;

public interface NovelService {
    List<NovelResponse> getAllNovels();
}
