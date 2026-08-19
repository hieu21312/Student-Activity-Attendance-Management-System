package com.nhom1.Nhom1_HTDiemDanh.controller;

import com.nhom1.Nhom1_HTDiemDanh.model.TaiKhoan;
import com.nhom1.Nhom1_HTDiemDanh.service.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private TaiKhoanService tkService;

    @GetMapping("/login")
    public String trangDangNhap(HttpSession session) {
        TaiKhoan user = (TaiKhoan) session.getAttribute("user");
        if (user != null) {
            if ("GiaoVu".equals(user.getVaiTro())) return "redirect:/gvk/dashboard";
            if ("BTC".equals(user.getVaiTro())) return "redirect:/btc/quan-ly";
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String xuLyDangNhap(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {

        TaiKhoan user = tkService.kiemTraDangNhap(username, password);

        if (user != null) {
            if (user.getTrangThai() == 0) {
                model.addAttribute("loi", "Tài khoản của bạn đã bị khóa!");
                return "login";
            }

            session.setAttribute("user", user);

            if ("GiaoVu".equals(user.getVaiTro())) return "redirect:/gvk/dashboard";
            if ("BTC".equals(user.getVaiTro())) return "redirect:/btc/quan-ly";

            return "redirect:/";
        }

        model.addAttribute("loi", "Sai tên đăng nhập hoặc mật khẩu!");
        return "login";
    }

    @GetMapping("/logout")
    public String dangXuat(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}