package jp.dkh634.springforum.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jp.dkh634.springforum.entity.Users;
import jp.dkh634.springforum.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userservice;
	
	@GetMapping("/api/user")
    public String displayUser(Model model) {
		List<Users> userList= new ArrayList<Users>();
		userList = getAllUsers();
    	//すべてのユーザーを取得(Todo　一旦固定値を返す)
    	model.addAttribute("AllUser", userList); 
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
    
    /*
     *  一覧を取得する処理
     *  @return userList
     */
    public List<Users> getAllUsers(){
    	List<Users> userList= new ArrayList<Users>();
    	userList = userservice.getAllusers();
    	
    	return userList;
    }
}
