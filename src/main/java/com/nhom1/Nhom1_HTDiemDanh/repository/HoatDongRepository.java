package com.nhom1.Nhom1_HTDiemDanh.repository;

import com.nhom1.Nhom1_HTDiemDanh.model.HoatDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository // Đánh dấu đây là tầng Data Access
public interface HoatDongRepository extends JpaRepository<HoatDong, String> {
    List<HoatDong> findByTrangThaiHanhChinh(Integer trangThaiHanhChinh);




}