package com.re.badminton_court.service.cluster_and_court;

import com.re.badminton_court.repository.BadmintonClusterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
ADMIN:
- Xem toàn bộ sân
- Có thể sửa/xóa mọi sân nếu cần

MANAGER:
- Thêm sân vào cụm sân mình quản lý
- Sửa sân thuộc cụm sân mình quản lý
- Bật/tắt trạng thái sân
- Upload/cập nhật ảnh sân

CUSTOMER:
- Chỉ xem danh sách sân khả dụng
- Xem chi tiết sân
- Không được CRUD
* */

@Service
@RequiredArgsConstructor
public class BadmintonClusterImpl {
    private final BadmintonClusterRepository clusterService;


}
