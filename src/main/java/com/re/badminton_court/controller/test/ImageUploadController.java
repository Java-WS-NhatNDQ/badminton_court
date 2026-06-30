package com.re.badminton_court.controller.test;

import com.re.badminton_court.model.dto.cloudinary.ImageResponse;
import com.re.badminton_court.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test/images")
public class ImageUploadController {
    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<ImageResponse> upload(@RequestParam("file") MultipartFile file) {
        try {
            ImageResponse response = cloudinaryService.uploadImage(file);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ImageResponse(null, null, e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ImageResponse(null, null, "Lỗi upload: " + e.getMessage()));
        }
    }
}
