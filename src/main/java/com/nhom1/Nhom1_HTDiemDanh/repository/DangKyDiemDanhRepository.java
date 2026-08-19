package com.nhom1.Nhom1_HTDiemDanh.repository;

import com.nhom1.Nhom1_HTDiemDanh.model.DangKyDiemDanh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository


public interface DangKyDiemDanhRepository extends JpaRepository<DangKyDiemDanh, Integer>{

    DangKyDiemDanh findFirstBySinhVien_MaSVAndHoatDong_MaHD(String maSV, String maHD);
    List<DangKyDiemDanh> findByHoatDong_MaHDAndLoaiDK(String maHD, int loaiDK);
    List<DangKyDiemDanh> findByHoatDong_MaHDAndLoaiDKOrderByThoiGianDangKyAsc(String maHD, int loaiDK);
    List<DangKyDiemDanh> findBySinhVien_MaSV(String maSV);
    List<DangKyDiemDanh> findByHoatDong_MaHD(String maHD);




}
