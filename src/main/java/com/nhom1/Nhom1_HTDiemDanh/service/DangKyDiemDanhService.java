package com.nhom1.Nhom1_HTDiemDanh.service;

import com.nhom1.Nhom1_HTDiemDanh.model.DangKyDiemDanh;
import com.nhom1.Nhom1_HTDiemDanh.model.HoatDong;
import com.nhom1.Nhom1_HTDiemDanh.model.SinhVien;
import com.nhom1.Nhom1_HTDiemDanh.repository.DangKyDiemDanhRepository;
import com.nhom1.Nhom1_HTDiemDanh.repository.HoatDongRepository;
import com.nhom1.Nhom1_HTDiemDanh.repository.SinhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DangKyDiemDanhService {

    @Autowired
    private DangKyDiemDanhRepository dangKyRepo;
    @Autowired
    private SinhVienRepository svRepo;
    @Autowired
    private HoatDongRepository hdRepo;

    public boolean dangKyHoatDong(String maSV, String maHD, int loaiDK, String ghiChu) {
        SinhVien sv = svRepo.findById(maSV).orElse(null);
        HoatDong hd = hdRepo.findById(maHD).orElse(null);

        if (sv == null || hd == null) return false;

        DangKyDiemDanh phieuDK = new DangKyDiemDanh();
        phieuDK.setIdDangKy(new Random().nextInt(10000) + 100);
        phieuDK.setSinhVien(sv);
        phieuDK.setHoatDong(hd);
        phieuDK.setLoaiDK(loaiDK); // Lưu đúng loại đăng ký
        phieuDK.setThoiGianDangKy(LocalDateTime.now());
        phieuDK.setGhiChu(ghiChu); // Lưu nội dung trình diễn/số người vào đây

        dangKyRepo.save(phieuDK);
        return true;
    }
    public boolean dangKyNhom(String maSVNhomTruong, String maHD, String noiDung, String danhSachMaSV) {

        // 1. Lưu đăng ký cho Nhóm trưởng trước (người đang đăng nhập)
        boolean kqTruongNhom = dangKyHoatDong(maSVNhomTruong, maHD, 1, "Nhóm trưởng - Tiết mục: " + noiDung);
        if (!kqTruongNhom) return false;

        // 2. Xử lý danh sách thành viên đi kèm (nếu có nhập)
        if (danhSachMaSV != null && !danhSachMaSV.trim().isEmpty()) {

            // Tách chuỗi dựa vào dấu phẩy. VD: "2001210002, 2001210003" -> Mảng ["2001210002", " 2001210003"]
            String[] cacThanhVien = danhSachMaSV.split(",");

            for (String maTV : cacThanhVien) {
                // Xóa khoảng trắng thừa (trim) để đảm bảo mã SV sạch sẽ
                String maSVSach = maTV.trim();

                // Lưu một dòng mới cho thành viên này, kèm ghi chú để biết thuộc nhóm ai
                dangKyHoatDong(maSVSach, maHD, 1, "Thành viên nhóm của " + maSVNhomTruong + " - Tiết mục: " + noiDung);
            }
        }
        return true;
    }
    // Hàm xử lý điểm danh cổ vũ kèm check thời gian
    public String diemDanhCoVu(String maSV, String maHD, String yKien) {
        DangKyDiemDanh dk = dangKyRepo.findFirstBySinhVien_MaSVAndHoatDong_MaHD(maSV, maHD);
        if (dk == null) return "Bạn chưa đăng ký tham gia sự kiện này!";

        HoatDong hd = dk.getHoatDong();
        if (hd.getThoiGianMoDiemDanhCV() == null) {
            return "BTC chưa mở form điểm danh cho sự kiện này!";
        }

        java.time.LocalDateTime mienThoiGianMo = hd.getThoiGianMoDiemDanhCV();
        java.time.LocalDateTime bayGio = java.time.LocalDateTime.now();

        // mienThoiGianMo.plusMinutes(10) nghĩa là lấy giờ mở cộng thêm 10 phút
        if (bayGio.isAfter(mienThoiGianMo.plusMinutes(10))) {
            return "Đã hết thời gian điểm danh! (Quá 10 phút)";
        }

        dk.setThoiGianDiemDanh(bayGio);
        dk.setKetQua(1); // 1 = Có mặt

        if (yKien != null && !yKien.isEmpty()) {
            dk.setGhiChu("Ý kiến: " + yKien);
        }

        dangKyRepo.save(dk);
        return "OK";
    }

    public void chotSoDiemDanhCoVu(String maHD) {
        // Lấy tất cả những bạn đăng ký CỔ VŨ (LoaiDK = 2)
        List<DangKyDiemDanh> dsCoVu = dangKyRepo.findByHoatDong_MaHDAndLoaiDK(maHD, 2);

        for (DangKyDiemDanh dk : dsCoVu) {
            if (dk.getKetQua() == null) {
                dk.setKetQua(0);
            }
            dangKyRepo.save(dk);
        }
    }
    // 1. Hàm lấy danh sách để đưa lên giao diện
    public List<DangKyDiemDanh> layDanhSachThamGia(String maHD) {
        return dangKyRepo.findByHoatDong_MaHDAndLoaiDK(maHD, 1);
    }

    // 2. Hàm xử lý điểm danh trực tiếp (Ánh xạ từ Sơ đồ lớp)
    public boolean diemDanhTrucTiep(String maHD, List<Integer> danhSachIdVang) {
        // Lấy lại toàn bộ danh sách đang diễn ra
        List<DangKyDiemDanh> danhSachDK = layDanhSachThamGia(maHD);

        for (DangKyDiemDanh dk : danhSachDK) {
            // Nếu form gửi về có chứa ID của người này -> Người này bị BTC tích là VẮNG
            if (danhSachIdVang != null && danhSachIdVang.contains(dk.getIdDangKy())) {
                dk.setKetQua(0); // 0 = Vắng mặt
            } else {
                // Nếu không bị tích vắng -> Có mặt
                dk.setKetQua(1); // 1 = Có mặt
            }
            // Ghi nhận thời gian điểm danh
            dk.setThoiGianDiemDanh(java.time.LocalDateTime.now());
            dangKyRepo.save(dk);
        }
        return true;
    }
    public Map<String, List<DangKyDiemDanh>> xuatDanhSachChot(String maHD) {
        // 1. Lấy thông tin Hoạt động để biết giới hạn số lượng là bao nhiêu
        HoatDong hd = hdRepo.findById(maHD).orElse(null);

        // 2. Lấy toàn bộ sinh viên đăng ký Tham gia (LoaiDK = 1), đã được xếp hàng từ trước đến sau
        List<DangKyDiemDanh> tatCaDangKy = dangKyRepo.findByHoatDong_MaHDAndLoaiDKOrderByThoiGianDangKyAsc(maHD, 1);

        // 3. Chuẩn bị 2 rổ chứa
        List<DangKyDiemDanh> danhSachChinhThuc = new ArrayList<>();
        List<DangKyDiemDanh> danhSachDuBi = new ArrayList<>();

        if (hd != null && tatCaDangKy != null) {
            int maxSoLuong = hd.getSoLuongToiDa();

            // 4. Lọc danh sách
            for (int i = 0; i < tatCaDangKy.size(); i++) {
                if (i < maxSoLuong) {
                    // Những người nằm trong giới hạn -> Chính thức
                    danhSachChinhThuc.add(tatCaDangKy.get(i));
                } else {
                    // Những người vượt quá giới hạn -> Dự bị
                    danhSachDuBi.add(tatCaDangKy.get(i));
                }
            }
        }

        // 5. Đóng gói cả 2 danh sách vào 1 cái Map để gửi lên Controller cho dễ
        Map<String, List<DangKyDiemDanh>> ketQua = new HashMap<>();
        ketQua.put("chinhThuc", danhSachChinhThuc);
        ketQua.put("duBi", danhSachDuBi);

        return ketQua;
    }
    // Hàm Giáo Vụ Khoa đối chiếu và tính điểm
    public List<DangKyDiemDanh> doiChieuDiemDanh(String maHD) {
        List<DangKyDiemDanh> danhSach = dangKyRepo.findByHoatDong_MaHD(maHD);
        for (DangKyDiemDanh dk : danhSach) {
            Integer ketQua = dk.getKetQua();
            int loaiDK = dk.getLoaiDK();

            if (ketQua != null) {
                if (ketQua == 1) {
                    if (loaiDK == 1) {
                        dk.setDiemCongTru(10); // Tham gia: +10
                    } else if (loaiDK == 2) {
                        dk.setDiemCongTru(3);  // Cổ vũ: +3
                    }
                } else if (ketQua == 0) {
                    dk.setDiemCongTru(-4);
                }
            } else {
                dk.setDiemCongTru(0);
            }

            dangKyRepo.save(dk);
        }

        return danhSach;
    }
    public List<DangKyDiemDanh> layLichSuCuaSinhVien(String maSV) {
        return dangKyRepo.findBySinhVien_MaSV(maSV);
    }
    public void tongHopDiemCuoiKy() {
        List<SinhVien> tatCaSinhVien = svRepo.findAll();

        for (SinhVien sv : tatCaSinhVien) {
            float diemTong = 70.0f; // Khởi điểm mỗi kỳ là 70

            List<DangKyDiemDanh> lichSu = layLichSuCuaSinhVien(sv.getMaSV());

            for (DangKyDiemDanh dk : lichSu) {
                if (dk.getDiemCongTru() != null) {
                    diemTong += dk.getDiemCongTru();
                }
            }

            if (diemTong > 100.0f) diemTong = 100.0f;
            if (diemTong < 0.0f) diemTong = 0.0f;

            sv.setTongDiemRL(diemTong);
            svRepo.save(sv);
        }
    }

}