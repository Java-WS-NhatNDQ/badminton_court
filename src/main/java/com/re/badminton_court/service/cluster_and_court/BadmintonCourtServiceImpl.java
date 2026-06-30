package com.re.badminton_court.service.cluster_and_court;

import com.re.badminton_court.repository.BadmintonCourtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
ADMIN:
- Tạo cụm sân
- Xem tất cả cụm sân
- Cập nhật cụm sân
- Xóa/khóa cụm sân
- Gán Manager cho cụm sân

MANAGER:
- Xem cụm sân mình quản lý
- Cập nhật thông tin cụm sân của mình
* */

@Service
@RequiredArgsConstructor
public class BadmintonCourtServiceImpl {
    private final BadmintonCourtRepository courtService;
}
