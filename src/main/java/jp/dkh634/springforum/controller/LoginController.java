package jp.dkh634.springforum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.dkh634.springforum.form.LoginForm;

@Controller
public class LoginController {

    /**
     * ログインページ表示用
     */
    @GetMapping("/api/login")
    public String getLogin(Model model) {
    	model.addAttribute("loginForm", new LoginForm());
    	return "login";
    }
}
