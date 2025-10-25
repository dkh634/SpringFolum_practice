package jp.dkh634.springforum.controller;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jp.dkh634.springforum.form.LoginForm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {

	@GetMapping("/api/login")
	public String login(Model model) {
	    model.addAttribute("title", "ログイン画面");
	    if (!model.containsAttribute("loginForm")) {
	        model.addAttribute("loginForm", new LoginForm());
	    }
	    return "/login";
	}


    @PostMapping("/api/login")
    public String doLogin(@Valid @ModelAttribute("loginForm") LoginForm loginForm, 
                          BindingResult bindingResult,
                          HttpSession session, 
                          RedirectAttributes redirectAttributes) {

        // ユーザー認証チェック
        if ("user".equals(loginForm.getUsername()) && "pass".equals(loginForm.getPassword())) {
            session.setAttribute("user", loginForm.getUsername());
            return "redirect:/api/thread";
        }

        // 認証失敗時はバリデーションエラーとして処理
        bindingResult.reject("login.invalid", "ユーザー名またはパスワードが違います");

        // Flash スコープにフォームデータとエラーを追加
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginForm", bindingResult);
        redirectAttributes.addFlashAttribute("loginForm", loginForm);

        return "redirect:/api/login";
    }

}

