package com.nhom1.Nhom1_HTDiemDanh.service;

import com.nhom1.Nhom1_HTDiemDanh.model.TaiKhoan;
import com.nhom1.Nhom1_HTDiemDanh.repository.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaiKhoanService {
    @Autowired
    private TaiKhoanRepository tkRepo;

    public TaiKhoan kiemTraDangNhap(String u, String p) {
        return tkRepo.findByTenDangNhapAndMatKhau(u, p);
    }
}
