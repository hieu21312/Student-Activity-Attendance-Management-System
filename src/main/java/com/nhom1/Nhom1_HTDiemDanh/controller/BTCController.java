package com.nhom1.Nhom1_HTDiemDanh.controller;

import com.nhom1.Nhom1_HTDiemDanh.model.HoatDong;
import com.nhom1.Nhom1_HTDiemDanh.model.NhanVien;
import com.nhom1.Nhom1_HTDiemDanh.model.TaiKhoan;
import com.nhom1.Nhom1_HTDiemDanh.repository.NhanVienRepository;
import com.nhom1.Nhom1_HTDiemDanh.service.HoatDongService;
import com.nhom1.Nhom1_HTDiemDanh.service.DangKyDiemDanhService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/btc")
public class BTCController {

    @Autowired
    private HoatDongService hoatDongService;

    @Autowired
    private DangKyDiemDanhService dangKyService;

    // Hiển thị Form tạo hoạt động
    @GetMapping("/tao-hoat-dong")
    public String hienThiFormTao(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"BTC".equals(user.getVaiTro())) return "redirect:/login";

        model.addAttribute("hoatDongMoi", new HoatDong());
        return "btc/tao-hoat-dong";
    }

    // Xử lý tạo hoạt động mới
    @PostMapping("/tao-hoat-dong")
    public String xuLyTaoHoatDong(@ModelAttribute("hoatDongMoi") HoatDong hoatDong, HttpSession session, RedirectAttributes ra) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"BTC".equals(user.getVaiTro())) return "redirect:/login";

        hoatDong.setTrangThaiHanhChinh(0);
        hoatDongService.luuHoatDong(hoatDong);

        ra.addFlashAttribute("thongBao", "Tạo sự kiện thành công! Hệ thống đã gửi yêu cầu chờ Giáo vụ khoa duyệt.");
        return "redirect:/btc/tao-hoat-dong";
    }

    // Trang quản lý sự kiện của BTC
    @GetMapping("/quan-ly")
    public String trangQuanLyBTC(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"BTC".equals(user.getVaiTro())) return "redirect:/login";

        model.addAttribute("danhSachHD", hoatDongService.getAllHoatDong());
        return "btc/quan-ly";
    }

    // Mở form điểm danh cổ vũ (Bắt đầu đếm ngược)
    @PostMapping("/mo-diem-danh")
    public String moNutDiemDanh(@RequestParam("maHD") String maHD, HttpSession session, RedirectAttributes ra) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"BTC".equals(user.getVaiTro())) return "redirect:/login";

        boolean kq = hoatDongService.moNutDiemDanhCoVu(maHD);
        if(kq) {
            ra.addFlashAttribute("thongBao", "Đã MỞ form điểm danh cho " + maHD + " ! Đồng hồ 10 phút bắt đầu đếm ngược!");
        }
        return "redirect:/btc/quan-ly";
    }

    // Chốt sổ điểm danh cổ vũ (Đánh vắng người không điểm danh)
    @PostMapping("/chot-so-co-vu")
    public String chotSoCoVu(@RequestParam("maHD") String maHD, HttpSession session, RedirectAttributes ra) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"BTC".equals(user.getVaiTro())) return "redirect:/login";

        dangKyService.chotSoDiemDanhCoVu(maHD);

        ra.addFlashAttribute("thongBao", "Đã chốt sổ điểm danh cổ vũ cho sự kiện " + maHD + ". Những ai không điểm danh đã bị đánh Vắng!");
        return "redirect:/btc/quan-ly";
    }

    // Mở trang điểm danh trực tiếp (biểu diễn)
    @GetMapping("/diem-danh-tham-gia")
    public String moTrangDiemDanh(@RequestParam("maHD") String maHD, HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"BTC".equals(user.getVaiTro())) return "redirect:/login";

        model.addAttribute("dsThamGia", dangKyService.layDanhSachThamGia(maHD));
        model.addAttribute("maHD", maHD);
        return "btc/diem-danh-truc-tiep";
    }

    // Lưu kết quả điểm danh trực tiếp
    @PostMapping("/luu-diem-danh-tham-gia")
    public String luuDiemDanh(@RequestParam("maHD") String maHD,
                              @RequestParam(value = "danhSachIdVang", required = false) List<Integer> danhSachIdVang,
                              HttpSession session, RedirectAttributes ra) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"BTC".equals(user.getVaiTro())) return "redirect:/login";

        dangKyService.diemDanhTrucTiep(maHD, danhSachIdVang);

        ra.addFlashAttribute("thongBao", "Đã lưu kết quả điểm danh trực tiếp thành công!");
        return "redirect:/btc/quan-ly";
    }

    @Autowired
    private NhanVienRepository nhanVienRepo;

    @GetMapping("/ho-so")
    public String xemHoSo(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        NhanVien nv = nhanVienRepo.findByTaiKhoan_MaTK(user.getMaTK());

        model.addAttribute("nv", nv);
        return "btc/ho-so";
    }
}