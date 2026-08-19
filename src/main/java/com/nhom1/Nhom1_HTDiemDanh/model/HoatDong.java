package com.nhom1.Nhom1_HTDiemDanh.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "HoatDong")
public class HoatDong {

    @Id
    @Column(name = "MaHD", length = 50)
    private String maHD;

    @Column(name = "TenHD")
    private String tenHD;

    @Column(name = "DiaDiem")
    private String diaDiem;

    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "NgayKetThuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "SoLuongToiDa")
    private Integer soLuongToiDa;

    @Column(name = "HanDangKy")
    private LocalDateTime hanDangKy;

    @Column(name = "ThoiGianMoDiemDanhCV")
    private LocalDateTime thoiGianMoDiemDanhCV;

    @Column(name = "NoiDungThongBao", columnDefinition = "NVARCHAR(MAX)")
    private String noiDungThongBao;

    @Column(name = "TrangThaiHanhChinh")
    private Integer trangThaiHanhChinh;

    @ManyToOne
    @JoinColumn(name = "NguoiDuyet", referencedColumnName = "MaNV")
    private NhanVien nguoiDuyet;
}
