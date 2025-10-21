package jp.dkh634.springforum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @GetMapping("/api/logout")
    public String logout(HttpSession session) {
        // セッション破棄
        session.invalidate();

        // ログインページへリダイレクト
        return "redirect:/api/login";
    }
}
