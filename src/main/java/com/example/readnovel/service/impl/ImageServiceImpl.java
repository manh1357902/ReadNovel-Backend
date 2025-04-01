package com.example.readnovel.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.readnovel.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final Cloudinary cloudinary;
    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);


    @Override
    public String uploadImage(MultipartFile file) {
        try {
            //up image lên folders uploads
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", "uploads"));
            String imageUrl = uploadResult.get("url").toString();
            logger.info("Image uploaded successfully: {}", imageUrl);
            return imageUrl;
        } catch (IOException e) {
            logger.error("Failed to upload image: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public void deleteImage(String imageUrl) {
        try {
            // Tách lấy publicId từ imageUrl
            String publicId = imageUrl.substring(imageUrl.lastIndexOf("uploads/"), imageUrl.lastIndexOf("."));

            // Gọi Cloudinary API để xóa ảnh
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            // Kiểm tra kết quả trả về
            String status = (String) result.get("result");
            if ("ok".equals(status)) {
                logger.info("Image deleted successfully: {}", publicId);
            } else {
                logger.warn("Failed to delete image: {} (status: {})", publicId, status);
            }
        } catch (IOException e) {
            logger.error("Error while deleting image {}: {}", imageUrl, e.getMessage(), e);
            throw new RuntimeException("Failed to delete image", e);
        }
    }
}
