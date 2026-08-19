package com.nhom1.Nhom1_HTDiemDanh.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "SinhVien")
public class SinhVien {

    @Id
    @Column(name = "MaSV", length = 50)
    private String maSV;

    @Column(name = "HoTen")
    private String hoTen;

    @Column(name = "Lop", length = 50)
    private String lop;

    @Column(name = "TongDiemRL")
    private Float tongDiemRL = 70.0f;

    // Ánh xạ khóa ngoại MaTK (Quan hệ 1-1 với TaiKhoan)
    @OneToOne
    @JoinColumn(name = "MaTK", referencedColumnName = "MaTK")
    private TaiKhoan taiKhoan;
}
