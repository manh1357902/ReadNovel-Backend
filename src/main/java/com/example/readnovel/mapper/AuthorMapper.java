package com.example.readnovel.mapper;

import com.example.readnovel.models.entity.Author;
import com.example.readnovel.payload.response.AuthorResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public AuthorResponse toAuthorResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .build();
    }
}
