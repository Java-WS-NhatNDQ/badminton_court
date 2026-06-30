package com.re.badminton_court.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.re.badminton_court.exception.CloudStorageException;
import com.re.badminton_court.exception.CloudStorageUnavailableException;
import com.re.badminton_court.model.dto.cloudinary.ImageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public ImageResponse uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String publicId = (String) uploadResult.get("public_id");
            String url = (String) uploadResult.get("secure_url");

            return new ImageResponse(publicId, url, "Success");
        } catch (SocketTimeoutException e) {
            log.error("Cloudinary connection timeout: {}", e.getMessage());
            throw new CloudStorageUnavailableException(
                    "Dịch vụ lưu trữ Cloudinary hiện không khả dụng (503) — quá thời gian chờ kết nối. Vui lòng thử lại sau.", e);
        } catch (UnknownHostException e) {
            log.error("Cloudinary DNS resolution failed: {}", e.getMessage());
            throw new CloudStorageUnavailableException(
                    "Dịch vụ lưu trữ Cloudinary hiện không khả dụng (503) — không thể phân giải tên miền. Vui lòng kiểm tra kết nối mạng.", e);
        } catch (IOException e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("503")) {
                log.error("Cloudinary returned 503 Service Unavailable: {}", msg);
                throw new CloudStorageUnavailableException(
                        "Dịch vụ lưu trữ Cloudinary hiện không khả dụng (503). Vui lòng thử lại sau.", e);
            }
            log.error("Cloudinary upload IO error: {}", e.getMessage());
            throw new CloudStorageException(
                    "Lỗi kết nối đến dịch vụ lưu trữ Cloudinary: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        } catch (RuntimeException e) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("503")) {
                log.error("Cloudinary returned 503 Service Unavailable: {}", msg);
                throw new CloudStorageUnavailableException(
                        "Dịch vụ lưu trữ Cloudinary hiện không khả dụng (503). Vui lòng thử lại sau.", e);
            }
            log.error("Cloudinary upload unexpected error: {}", e.getMessage());
            throw new CloudStorageException(
                    "Lỗi không xác định từ dịch vụ lưu trữ Cloudinary: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
