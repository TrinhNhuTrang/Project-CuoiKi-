# Việt Hành Backend

REST API địa điểm cho ứng dụng Việt Hành. Project dùng Java 21, Spring Boot, Spring JDBC và H2 trong bộ nhớ. Khi khởi động, H2 nạp dữ liệu mẫu từ `src/main/resources/data.sql`. Các trường ảnh, tọa độ và giá vé trong mẫu để trống.

## Chạy backend

Mở thư mục `app/backend` dưới dạng project Maven trong IntelliJ IDEA và chạy lớp `VietHanhApplication`. Nếu Maven có trên PATH, chạy lệnh sau trong thư mục này.

```powershell
mvn spring-boot:run
```

Hoặc đóng gói và chạy JAR bằng Java 21.

```powershell
mvn package
java -jar target/viethanh-backend-0.0.1-SNAPSHOT.jar
```

## Thử API

| Yêu cầu | Kết quả |
| --- | --- |
| `GET /api/dia-diem` | Tất cả địa điểm mẫu |
| `GET /api/dia-diem?tinhId=2&loaiId=1` | Địa điểm loại Thiên nhiên ở Lâm Đồng |
| `GET /api/dia-diem?tuKhoa=Hội%20An` | Tìm theo tên hoặc địa chỉ |
| `GET /api/dia-diem/1` | Chi tiết địa điểm số 1 |
| `GET /api/dia-diem/bo-loc` | Danh sách tỉnh và loại kèm ID dùng cho bộ lọc |

Các bộ lọc có thể dùng cùng nhau. Bỏ tham số `tinhId` hoặc `loaiId`, hoặc gửi `0`, để chọn **Tất cả** trong nhóm đó. API trả `404` nếu ID chi tiết không tồn tại.

```powershell
curl.exe "http://localhost:8080/api/dia-diem?tinhId=2&loaiId=1"
```

Backend chạy độc lập với app Android. Dữ liệu mẫu được khởi tạo lại mỗi lần chạy.
