package com.nhom1.Nhom1_HTDiemDanh.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {

    @Id
    @Column(name = "MaTK", length = 50)
    private String maTK;

    @Column(name = "TenDangNhap", unique = true)
    private String tenDangNhap;

    @Column(name = "MatKhau")
    private String matKhau;

    @Column(name = "VaiTro")
    private String vaiTro; // Các giá trị: "SV", "BTC", "GVK"

    @Column(name = "TrangThai")
    private Integer trangThai = 1; // 1: Hoạt động, 0: Khóa
}