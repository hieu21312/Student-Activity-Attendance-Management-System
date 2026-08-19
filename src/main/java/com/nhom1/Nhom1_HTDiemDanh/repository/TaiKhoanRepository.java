package com.nhom1.Nhom1_HTDiemDanh.repository;


import com.nhom1.Nhom1_HTDiemDanh.model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    TaiKhoan findByTenDangNhapAndMatKhau(String u, String p);
}