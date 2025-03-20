package com.example.readnovel.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer chapterNumber;
    private String title;
    private String content;
    private Boolean isLocked;
    private Double priceUnlock;
    private Integer views;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id")
    private Novel novel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PrePersist
    private void created(){
        this.createdAt = LocalDateTime.now();
    }
    @PreUpdate
    private void updated(){
        this.updatedAt = LocalDateTime.now();
    }
}
