package com.nhom1.Nhom1_HTDiemDanh.repository;

import com.nhom1.Nhom1_HTDiemDanh.model.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {
    NhanVien findByTaiKhoan_MaTK(String maTK);
}
