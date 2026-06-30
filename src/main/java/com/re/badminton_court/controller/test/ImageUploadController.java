package com.re.badminton_court.controller.test;

import com.re.badminton_court.model.dto.ApiResponse;
import com.re.badminton_court.model.dto.cloudinary.ImageResponse;
import com.re.badminton_court.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test/images")
public class ImageUploadController {
    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ImageResponse>> upload(@RequestParam("file") MultipartFile file) {
        ImageResponse response = cloudinaryService.uploadImage(file);
        return ResponseEntity.ok(ApiResponse.success(response, "Upload ảnh thành công"));
    }
}
