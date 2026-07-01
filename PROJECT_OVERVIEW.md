# Tong quan du an badminton_court

## 1. Muc tieu

`badminton_court` la he thong Java Spring Boot dung de quan ly cum san cau long, san, khung gio, nguoi dung va dat lich. He thong phan quyen theo 3 nhom chinh:

- `ADMIN`: quan tri nguoi dung, cum san, san, time slot va booking.
- `MANAGER`: quan ly cum san/san/anh san/booking thuoc minh so huu.
- `CUSTOMER`: xem san, dat san va quan ly lich dat cua minh.

## 2. Cong nghe chinh

- Java 17 theo cau hinh Gradle toolchain.
- Spring Boot, Spring Web MVC, Spring Data JPA, Spring Security.
- JWT cho xac thuc access token va refresh token.
- MySQL la database chinh.
- Redis co dependency cho token/cache lien quan.
- Cloudinary dung cho upload anh san.
- AOP dung cho audit booking va log thoi gian thuc hien.
- Gradle va Jacoco cho build/test/coverage.

## 3. Cau truc package

- `controller`: nhan request, goi service, tra `ApiResponse`.
- `service`: xu ly nghiep vu chinh.
- `repository`: Spring Data JPA repository.
- `model.entity`: entity map voi database.
- `model.dto`: request/response DTO, tach khoi entity.
- `model.enums`: enum `Role`, `BookingStatus`.
- `security`: JWT filter, token provider, user details.
- `config`: security va cau hinh Cloudinary.
- `exception`: exception rieng va `GlobalExceptionHandler`.
- `aspect`: logging/audit bang AOP.

## 4. Dinh dang response va exception

Tat ca API nen tra ve `ApiResponse<T>`:

- Thanh cong: `ApiResponse.success(data, message)`.
- Loi: `ApiResponse.error(message)`.

`GlobalExceptionHandler` dang xu ly cac nhom loi chinh:

- `ResourceNotFoundException` tra `404`.
- `IllegalArgumentException` tra `400`.
- Validation request tra `400`.
- User bi khoa tra `403`.
- Loi Cloudinary tra status tuong ung.
- Loi khac tra `500`.

## 5. Bao mat va phan quyen

Security duoc cau hinh trong `SecurityConfig`:

- Public:
  - `/api/v1/auth/**`
  - `/api/v1/courts/**`
  - `/api/v1/clusters/**`
  - `/api/v1/time-slots/**`
  - `/api/v1/test/**`
- Chi `ADMIN`: `/api/v1/admin/**`
- Chi `MANAGER`: `/api/v1/manager/**`
- Chi `CUSTOMER`: `/api/v1/customer/**`

He thong dung JWT stateless, filter `JwtAuthenticationFilter` chay truoc `UsernamePasswordAuthenticationFilter`.

## 6. Nhom API chinh

### Auth

- Dang ky, dang nhap, refresh token, dang xuat.
- Quen mat khau va doi mat khau.

### Public

- Xem danh sach san.
- Xem chi tiet san.
- Xem san theo cum san.
- Xem san kha dung theo ngay/time slot.
- Xem danh sach time slot active.

### Admin

- CRUD user, ban/unban user.
- CRUD cum san.
- Gan manager cho cum san.
- CRUD san.
- Cap nhat trang thai san kha dung.
- CRUD time slot.
- Xem, confirm, cancel booking.

### Manager

- Xem/sua cum san minh quan ly.
- CRUD san thuoc cum san minh quan ly.
- Upload/xoa anh san thuoc san minh quan ly.
- Xem, confirm, cancel, check-in booking thuoc cum san minh quan ly.

### Customer

- Tao booking.
- Xem lich su booking cua minh.
- Xem chi tiet booking cua minh.
- Huy booking cua minh theo dieu kien service.

## 7. Nghiep vu quan trong

### Dat san

Khi customer dat san:

- Court phai ton tai.
- Court phai dang available.
- TimeSlot phai active.
- Khong duoc trung booking co status `PENDING` hoac `CONFIRMED`.
- Booking moi co status ban dau `PENDING`.

### Manager ownership

Manager chi thao tac duoc voi cluster/court/booking thuoc quyen so huu:

```text
cluster.manager.id == currentUser.id
```

### TimeSlot

- API public chi xem danh sach active.
- Admin quan ly CRUD.
- Service da chan trung `label` hoac `startTime` khi tao/cap nhat.

### Court by Cluster

API public lay courts theo cluster se kiem tra cluster ton tai truoc. Neu cluster khong ton tai, he thong tra `404`.

## 8. Logging va audit

Logging duoc cau hinh trong `application.properties`:

- Console hien log package `com.re.badminton_court` o muc `DEBUG`.
- File log duoc ghi vao `logs/badminton-court.log`.
- Cau hinh rolling:
  - Moi file toi da `10MB`.
  - Giu lich su `14` file.
  - Tong dung luong toi da `100MB`.

`LoggingAspect` co 2 nhom log:

- Performance log cho controller/service/repository:
  - Thanh cong: `[PERFORMANCE] ... completed in ... ms`
  - Loi: `[PERFORMANCE] ... failed in ... ms`
- Audit log rieng cho booking:
  - Tao booking thanh cong.
  - Tao booking that bai.

Luu y: business method dat san khong ghi log truc tiep, audit duoc tach bang AOP.

## 9. Database va seed data

- Cau hinh database MySQL nam trong `application.properties`.
- File seed data: `src/main/resources/db/data-init.sql`.
- `spring.sql.init.mode=never` nen seed data khong tu chay mac dinh trong moi lan start.

## 10. Test va coverage

Project da cau hinh Jacoco:

- Chay test: `./gradlew test`
- Tao coverage report: `./gradlew jacocoTestReport`
- HTML report: `build/reports/jacoco/test/html/index.html`
- XML report: `build/reports/jacoco/test/jacocoTestReport.xml`

Luu y moi truong hien tai can cai JDK 17 de Gradle toolchain compile/test thanh cong.

## 11. Luu y van hanh

- Khong xoa endpoint `/api/v1/test/**` vi dang duoc cau hinh public.
- Can cau hinh bien moi truong cho database, JWT, mail va Cloudinary.
- Khi them API moi, nen giu controller mong, dua nghiep vu vao service.
- Khi them DTO moi, khong tra entity truc tiep ra API.
- Khi them rule phan quyen moi, can dong bo voi `SecurityConfig` va ownership check trong service.
