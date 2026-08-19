package com.nhom1.Nhom1_HTDiemDanh.controller;

import com.nhom1.Nhom1_HTDiemDanh.model.DangKyDiemDanh;
import com.nhom1.Nhom1_HTDiemDanh.model.HoatDong;
import com.nhom1.Nhom1_HTDiemDanh.model.SinhVien;
import com.nhom1.Nhom1_HTDiemDanh.model.TaiKhoan;
import com.nhom1.Nhom1_HTDiemDanh.service.HoatDongService;
import com.nhom1.Nhom1_HTDiemDanh.service.DangKyDiemDanhService;
import com.nhom1.Nhom1_HTDiemDanh.repository.SinhVienRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private HoatDongService hoatDongService;

    @Autowired
    private DangKyDiemDanhService dangKyService;

    @Autowired
    private SinhVienRepository svRepo;

    @GetMapping("/")
    public String trangChu(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<HoatDong> danhSachHD = hoatDongService.getAllHoatDong();
        model.addAttribute("danhSachHD", danhSachHD);
        model.addAttribute("user", user);

        return "home/index";
    }

    @GetMapping("/chi-tiet")
    public String xemChiTietHoatDong(@RequestParam("maHD") String maHD, HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        HoatDong hd = hoatDongService.getHoatDongById(maHD);
        model.addAttribute("hd", hd);
        return "home/chi-tiet";
    }

    @GetMapping("/dang-ky-co-vu")
    public String dangKyCoVu(String maHD, HttpSession session, RedirectAttributes ra) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        boolean kq = dangKyService.dangKyHoatDong(user.getTenDangNhap(), maHD, 2, null);

        if(kq) ra.addFlashAttribute("thongBao", "Đăng ký CỔ VŨ thành công!");
        return "redirect:/";
    }

    @GetMapping("/form-tham-gia")
    public String moFormThamGia(String maHD, HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        model.addAttribute("maHD", maHD);
        return "home/form-tham-gia";
    }

    @PostMapping("/xu-ly-tham-gia")
    public String xuLyThamGia(@RequestParam("maHD") String maHD,
                              @RequestParam("noiDung") String noiDung,
                              @RequestParam(value = "danhSachMaSV", required = false) String danhSachMaSV,
                              HttpSession session, RedirectAttributes ra) {

        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        boolean kq = dangKyService.dangKyNhom(user.getTenDangNhap(), maHD, noiDung, danhSachMaSV);

        if(kq) {
            ra.addFlashAttribute("thongBao", "Đăng ký tiết mục thành công cho toàn bộ nhóm!");
            ra.addFlashAttribute("trangThai", "xanh");
        } else {
            ra.addFlashAttribute("thongBao", "Có lỗi xảy ra, vui lòng thử lại!");
            ra.addFlashAttribute("trangThai", "do");
        }
        return "redirect:/";
    }

    @GetMapping("/form-diem-danh")
    public String hienThiFormDiemDanh(@RequestParam("maHD") String maHD, HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        model.addAttribute("maHD", maHD);
        return "home/form-diem-danh";
    }

    @PostMapping("/xu-ly-diem-danh")
    public String xuLyDiemDanhCoVu(@RequestParam("maHD") String maHD,
                                   @RequestParam(value = "yKien", required = false) String yKien,
                                   HttpSession session, RedirectAttributes ra) {

        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        String ketQua = dangKyService.diemDanhCoVu(user.getTenDangNhap(), maHD, yKien);

        if (ketQua.equals("OK")) {
            ra.addFlashAttribute("thongBao", "Điểm danh thành công! Cảm ơn bạn đã tham gia.");
            ra.addFlashAttribute("trangThai", "xanh");
        } else {
            ra.addFlashAttribute("thongBao", "Lỗi: " + ketQua);
            ra.addFlashAttribute("trangThai", "do");
        }
        return "redirect:/";
    }

    @GetMapping("/xem-danh-sach")
    public String xemDanhSachThamGia(@RequestParam("maHD") String maHD, HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        HoatDong hd = hoatDongService.getHoatDongById(maHD);
        Map<String, List<DangKyDiemDanh>> mapDanhSach = dangKyService.xuatDanhSachChot(maHD);

        model.addAttribute("hd", hd);
        model.addAttribute("dsChinhThuc", mapDanhSach.get("chinhThuc"));
        model.addAttribute("dsDuBi", mapDanhSach.get("duBi"));

        return "home/danh-sach-tham-gia";
    }

    @GetMapping("/ho-so")
    public String xemHoSoCaNhan(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        String maSVDangNhap = user.getTenDangNhap();

        SinhVien sv = svRepo.findById(maSVDangNhap).orElse(null);
        List<DangKyDiemDanh> lichSu = dangKyService.layLichSuCuaSinhVien(maSVDangNhap);

        model.addAttribute("sv", sv);
        model.addAttribute("lichSu", lichSu);

        return "home/ho-so";
    }
}