package jp.dkh634.springforum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/api/login")
    public String login(Model model) {
        
    	//タイトルを入れる(消してもいい)
    	model.addAttribute("title", "ログイン画面"); 
        // ログインページへリダイレクト
        return "login";
    }
    
    // ログイン処理
    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {

        // 仮の認証処理
        if ("user".equals(username) && "pass".equals(password)) {
            session.setAttribute("user", username); // セッションに保存
            return "redirect:/home"; // ログイン成功
        } else {
            model.addAttribute("title", "ログイン画面");
            model.addAttribute("error", "ユーザー名またはパスワードが違います");
            return "login"; // 再表示
        }
    }
}
