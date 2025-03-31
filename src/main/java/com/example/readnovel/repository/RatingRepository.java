package com.example.readnovel.repository;

import com.example.readnovel.models.entity.Rating;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository {

    List<Rating> findAllByNovelId(Long novelId);
}
