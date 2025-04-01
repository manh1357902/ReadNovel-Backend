package com.example.readnovel.repository;

import com.example.readnovel.models.entity.Novel;
import com.example.readnovel.models.enumn.NovelStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovelRepository extends JpaRepository<Novel, Long> {
    @Query("SELECT DISTINCT n FROM Novel n "+
            "WHERE n.isDeleted = false "+
            "AND (:title IS NULL OR n.title LIKE CONCAT('%', REPLACE(:title, '@', '\\\\@'), '%') ESCAPE '\\') " +
            "AND (:status IS NULL OR n.status = :status) " +
            "AND (:categoryIds IS NULL OR EXISTS (SELECT 1 FROM n.categories c WHERE c.id IN :categoryIds)) " +
            "AND (:minChapter IS NULL OR (SELECT COUNT(ch) FROM Chapter ch WHERE ch.novel = n) >= :minChapter) " +
            "AND (:maxChapter IS NULL OR (SELECT COUNT(ch) FROM Chapter ch WHERE ch.novel = n) <= :maxChapter) " +
            "AND EXISTS (SELECT 1 FROM Chapter c WHERE c.novel = n) "
    )
    Page<Novel> getNovelsFilter(@Param("title") String title,
                                @Param("status") NovelStatusEnum status,
                                @Param("categoryIds") List<Integer> categoryIds,
                                @Param("minChapter") Integer minChapter,
                                @Param("maxChapter") Integer maxChapter,
                                Pageable pageable);


    @Query("SELECT n FROM Novel n " +
            "WHERE n.isDeleted = false " +
            "AND EXISTS (SELECT 1 FROM Chapter c WHERE c.novel = n) " +
            "ORDER BY (SELECT MAX(c.createdAt) FROM Chapter c WHERE c.novel = n) DESC"
    )
    Page<Novel> getAllByIsDeletedFalse(Pageable pageable);
}
