package com.nhom1.Nhom1_HTDiemDanh.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name = "NhanVien")
public class NhanVien {

    @Id
    @NotBlank(message = "Mã nhân viên không được để trống!")
    @Pattern(regexp = "^(NV|DH)\\d+$", message = "Mã nhân viên phải bắt đầu bằng NV hoặc DH, kèm theo số (VD: NV01, DH02)")
    @Column(name = "MaNV", unique = true, length = 50)
    private String maNV;

    @Column(name = "HoTen")
    private String hoTen;

    @Column(name = "ChucVu")
    private String chucVu;

    @OneToOne
    @JoinColumn(name = "MaTK", referencedColumnName = "MaTK")
    private TaiKhoan taiKhoan;
}