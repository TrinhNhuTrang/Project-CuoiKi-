CREATE TABLE tinh (
    id INT PRIMARY KEY,
    ten VARCHAR(100) NOT NULL
);

CREATE TABLE loai_dia_diem (
    id INT PRIMARY KEY,
    ten VARCHAR(100) NOT NULL
);

CREATE TABLE dia_diem (
    id INT PRIMARY KEY,
    ten VARCHAR(200) NOT NULL,
    dia_chi VARCHAR(300) NOT NULL,
    tinh_id INT NOT NULL REFERENCES tinh(id),
    loai_id INT NOT NULL REFERENCES loai_dia_diem(id),
    mo_ta VARCHAR(1000) NOT NULL,
    vi_do DOUBLE PRECISION,
    kinh_do DOUBLE PRECISION,
    hinh_anh VARCHAR(500),
    gia_ve DECIMAL(12, 2)
);
