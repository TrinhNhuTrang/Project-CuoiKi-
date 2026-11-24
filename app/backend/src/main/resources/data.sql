INSERT INTO tinh (id, ten) VALUES
    (1, 'Quảng Nam'),
    (2, 'Lâm Đồng'),
    (3, 'Kiên Giang');

INSERT INTO loai_dia_diem (id, ten) VALUES
    (1, 'Thiên nhiên'),
    (2, 'Di tích văn hóa'),
    (3, 'Nghỉ dưỡng biển');

INSERT INTO dia_diem (id, ten, dia_chi, tinh_id, loai_id, mo_ta) VALUES
    (1, 'Đồi chè Cầu Đất', 'Đà Lạt, Lâm Đồng', 2, 1,
     'Điểm tham quan với những đồi chè và cảnh quan cao nguyên.'),
    (3, 'Thác Datanla', 'Đà Lạt, Lâm Đồng', 2, 1,
     'Điểm tham quan thác nước trong khu vực rừng thông.'),
    (4, 'Phố cổ Hội An', 'Hội An, Quảng Nam', 1, 2,
     'Khu phố cổ thích hợp cho hành trình đi bộ và tìm hiểu văn hóa.'),
    (5, 'Cù Lao Chàm', 'Hội An, Quảng Nam', 1, 3,
     'Gợi ý cho chuyến đi khám phá biển đảo.'),
    (6, 'Bãi Sao', 'Phú Quốc, Kiên Giang', 3, 3,
     'Bãi biển phù hợp cho một chuyến nghỉ dưỡng.');

