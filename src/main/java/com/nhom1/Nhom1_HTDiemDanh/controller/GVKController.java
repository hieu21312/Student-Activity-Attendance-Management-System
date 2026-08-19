package com.nhom1.Nhom1_HTDiemDanh.controller;

import com.nhom1.Nhom1_HTDiemDanh.model.DangKyDiemDanh;
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
@RequestMapping("/gvk")
public class GVKController {

    @Autowired
    private HoatDongService hoatDongService;

    @Autowired
    private DangKyDiemDanhService dangKyService;

    @GetMapping("/dashboard")
    public String trangChuGVK(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"GiaoVu".equals(user.getVaiTro())) return "redirect:/login";

        model.addAttribute("tatCaHD", hoatDongService.getAllHoatDong());

        return "gvk/dashboard";
    }
    // Hiển thị danh sách hoạt động chờ duyệt
    @GetMapping("/duyet-hoat-dong")
    public String hienThiDanhSachChoDuyet(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"GiaoVu".equals(user.getVaiTro())) return "redirect:/login";

        List<HoatDong> danhSachCho = hoatDongService.getHoatDongChoDuyet();
        model.addAttribute("danhSachCho", danhSachCho);
        return "gvk/duyet-hoat-dong";
    }

    // Xử lý hành động Duyệt hoặc Từ chối hoạt động
    @PostMapping("/xu-ly-duyet")
    public String xuLyDuyet(@RequestParam("maHD") String maHD,
                            @RequestParam("hanhDong") String hanhDong,
                            HttpSession session, RedirectAttributes ra) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"GiaoVu".equals(user.getVaiTro())) return "redirect:/login";

        int trangThaiMoi = 0;
        if (hanhDong.equals("duyet")) {
            trangThaiMoi = 1;
            ra.addFlashAttribute("thongBao", "Đã DUYỆT hoạt động " + maHD);
        } else if (hanhDong.equals("tuchoi")) {
            trangThaiMoi = 2;
            ra.addFlashAttribute("thongBao", "Đã TỪ CHỐI hoạt động " + maHD);
        }

        hoatDongService.xetDuyetHoatDong(maHD, trangThaiMoi, user.getTenDangNhap());

        return "redirect:/gvk/duyet-hoat-dong";
    }

    // Mở trang đối chiếu và tính điểm cho sinh viên
    @GetMapping("/doi-chieu-diem")
    public String xemDoiChieuDiem(@RequestParam("maHD") String maHD, HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"GiaoVu".equals(user.getVaiTro())) return "redirect:/login";

        List<DangKyDiemDanh> dsDoiChieu = dangKyService.doiChieuDiemDanh(maHD);
        model.addAttribute("dsDoiChieu", dsDoiChieu);
        model.addAttribute("maHD", maHD);

        return "gvk/doi-chieu";
    }

    // Xử lý tổng hợp và chốt điểm rèn luyện cuối kỳ
    @PostMapping("/chot-diem-cuoi-ky")
    public String chotDiemCuoiKy(HttpSession session, RedirectAttributes ra) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"GiaoVu".equals(user.getVaiTro())) return "redirect:/login";

        dangKyService.tongHopDiemCuoiKy();
        ra.addFlashAttribute("thongBao", "Đã tổng hợp điểm rèn luyện cuối kỳ cho TOÀN BỘ sinh viên thành công!");
        return "redirect:/gvk/duyet-hoat-dong";
    }

    // Mở giao diện chốt điểm rèn luyện
    @GetMapping("/quan-ly-diem")
    public String trangChotDiem(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null || !"GiaoVu".equals(user.getVaiTro())) return "redirect:/login";

        return "gvk/chot-diem";
    }
    @Autowired
    private NhanVienRepository nhanVienRepo;

    @GetMapping("/ho-so")
    public String xemHoSo(HttpSession session, Model model) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        NhanVien nv = nhanVienRepo.findByTaiKhoan_MaTK(user.getMaTK());

        model.addAttribute("nv", nv);
        return "gvk/ho-so";
    }
}