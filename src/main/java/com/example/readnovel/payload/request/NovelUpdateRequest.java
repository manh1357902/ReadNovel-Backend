package com.example.readnovel.payload.request;

import com.example.readnovel.constrains.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class NovelUpdateRequest {
    @NotBlank(message = Message.TITLE_NOT_BLANK)
    private String title;
    @NotBlank(message = Message.DESCRIPTION_NOT_BLANK)
    private String description;
    private MultipartFile image;
    @NotBlank(message = Message.AUTHOR_NOT_BLANK)
    private String author;
    @NotNull(message = Message.CATEGORY_NOT_EMPTY)
    private List<Long> categoryIds;
}
