package com.example.readnovel.utils;

import org.springframework.stereotype.Component;

@Component
public class SlugUtils {
    public String generateSlug(String title) {
        // Chuyển sang chữ thường
        String slug = title.toLowerCase();

        // đổi khoảng trắng thành dấu gạch ngang
        slug = slug.replaceAll(" ", "-");

        // xóa ký tự đặc biệt
        slug = slug.replaceAll("[^a-z0-9-]", "");

        //xóa các dấu gạch ngang liên tiếp
        slug = slug.replaceAll("-{2,}", "-");

        // Xóa dấu gạch ngang ở đầu cuối
        slug = slug.replaceAll("^-|-$", "");

        return slug;
    }
}
