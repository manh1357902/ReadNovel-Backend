package com.example.readnovel.models.entity;

import com.example.readnovel.models.enumn.NovelStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Novel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT", length = 3000)
    private String description;
    private String slug;
    private String imageUrl;
    private Long recommendCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "author_id")
    private Author author;
    @Enumerated(EnumType.STRING)
    private NovelStatusEnum status;
    @ManyToMany(mappedBy = "novels", fetch = FetchType.LAZY)
    private List<Category> categories;
    private Boolean isDeleted;
    private Integer chapterPerWeek;
    @PrePersist
    private void createUser(){
        this.createdAt = LocalDateTime.now();
    }
    @PreUpdate
    private void updateUser(){
        this.updatedAt = LocalDateTime.now();
    }
}
