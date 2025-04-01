package com.example.readnovel.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private LocalDate birthDate;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns =  @JoinColumn(name = "role_id"))
    private List<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    private void createUser(){
        this.createdAt = LocalDateTime.now();
    }
    @PreUpdate
    private void updateUser(){
        this.updatedAt = LocalDateTime.now();
    }
}
