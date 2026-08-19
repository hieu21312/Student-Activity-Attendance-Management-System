package com.nhom1.Nhom1_HTDiemDanh.service;

import com.nhom1.Nhom1_HTDiemDanh.model.HoatDong;
import com.nhom1.Nhom1_HTDiemDanh.repository.HoatDongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Báo cho Spring Boot biết đây là tầng Service
public class HoatDongService {

    @Autowired // Kỹ thuật Dependency Injection: Tự động nhúng Repository vào đây
    private HoatDongRepository hoatDongRepository;


    public List<HoatDong> getAllHoatDong() {
        // Gọi hàm findAll() có sẵn của JPA để lấy toàn bộ dữ liệu trong bảng HoatDong
        return hoatDongRepository.findAll();
    }
    public HoatDong getHoatDongById(String maHD) {
        // Dùng Optional.orElse(null) để lỡ không tìm thấy thì nó không bị lỗi sập app
        return hoatDongRepository.findById(maHD).orElse(null);
    }
    public void luuHoatDong(HoatDong hoatDong) {
        hoatDongRepository.save(hoatDong);
    }
    public List<HoatDong> getHoatDongChoDuyet() {
        return hoatDongRepository.findByTrangThaiHanhChinh(0);
    }

    // 2. Hàm xử lý Duyệt hoặc Từ chối
    public boolean xetDuyetHoatDong(String maHD, int trangThaiMoi, String maGVDuyet) {
        // Tìm hoạt động trong DB
        HoatDong hd = hoatDongRepository.findById(maHD).orElse(null);
        if (hd != null) {
            hd.setTrangThaiHanhChinh(trangThaiMoi); // 1 = Đã duyệt, 2 = Từ chối
            // Lưu ý: Ở đây lý tưởng nhất là gán luôn NguoiDuyet, nhưng để đơn giản
            // tạm thời chúng ta chỉ update trạng thái trước nhé.
            hoatDongRepository.save(hd);
            return true;
        }
        return false;
    }
    // BTC mở nút điểm danh cho khán giả cổ vũ
    public boolean moNutDiemDanhCoVu(String maHD) {
        HoatDong hd = hoatDongRepository.findById(maHD).orElse(null);
        if (hd != null) {
            // Cập nhật mốc thời gian bắt đầu tính giờ
            hd.setThoiGianMoDiemDanhCV(java.time.LocalDateTime.now());
            hoatDongRepository.save(hd);
            return true;
        }
        return false;
    }
}
