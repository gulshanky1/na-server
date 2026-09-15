package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        try {

            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "image",
                            "folder", "nadi-astrology"
                    )
            );

            return result.get("secure_url").toString();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload image to Cloudinary",
                    e
            );
        }
    }
}