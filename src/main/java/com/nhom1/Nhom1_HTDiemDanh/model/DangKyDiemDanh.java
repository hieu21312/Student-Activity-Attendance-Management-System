package com.nhom1.Nhom1_HTDiemDanh.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "DangKyDiemDanh")
public class DangKyDiemDanh {

    @Id
    @Column(name = "ID_DangKy")
    private Integer idDangKy;

    // Khóa ngoại MaSV (Nhiều lượt đăng ký thuộc về 1 Sinh Viên)
    @ManyToOne
    @JoinColumn(name = "MaSV", referencedColumnName = "MaSV")
    private SinhVien sinhVien;

    // Khóa ngoại MaHD (Nhiều lượt đăng ký thuộc về 1 Hoạt Động)
    @ManyToOne
    @JoinColumn(name = "MaHD", referencedColumnName = "MaHD")
    private HoatDong hoatDong;

    @Column(name = "LoaiDK")
    private Integer loaiDK;

    @Column(name = "GhiChu", columnDefinition = "NVARCHAR(MAX)")
    private String ghiChu;

    @Column(name = "ThoiGianDangKy")
    private LocalDateTime thoiGianDangKy;

    @Column(name = "ThoiGianDiemDanh")
    private LocalDateTime thoiGianDiemDanh;

    @Column(name = "KetQua")
    private Integer ketQua;

    @Column(name = "DiemCongTru")
    private Integer diemCongTru;

}