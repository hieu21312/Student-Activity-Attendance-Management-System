package com.nhom1.Nhom1_HTDiemDanh.repository;

import com.nhom1.Nhom1_HTDiemDanh.model.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, String>{
}
