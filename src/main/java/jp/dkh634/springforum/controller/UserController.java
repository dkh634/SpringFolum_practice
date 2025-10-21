package jp.dkh634.springforum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
//	@Autowired
//	private userService userservice;
	
	@GetMapping("/api/user")
    public String displayUser(Model model) {
        
//		userservice.getAllUser();
    	//すべてのユーザーを取得(Todo　一旦固定値を返す)
    	model.addAttribute("AllUser", "ユーザーリスト"); 
        // ログインページへリダイレクト
        return "user";
    }
    
    // ログイン処理
    @PostMapping("/api/user")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {
            return "user"; // 再表示
    }
}
