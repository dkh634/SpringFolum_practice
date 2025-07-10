package jp.dkh634.springforum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jp.dkh634.springforum.entity.Post;
import jp.dkh634.springforum.form.ForumPostForm;
import jp.dkh634.springforum.service.ForumService;

@Controller
public class ForumController {
	@Autowired
    private ForumService service;
	
	@PostMapping("/api/post")
	public String reservePost(@ModelAttribute ForumPostForm postForm,Model model) {
		
		//受け取れているかチェックTodo；後で消す
		System.out.println(postForm.getAuthorName());
		System.out.println(postForm.getContent());
		
		//FormからEntityに詰め替える
		Post post =service.toEntity(postForm);
		
		//DB処理を入れる
		Post postedData = service.saveDateBase(post);
		
		
		//modelに格納する
		model.addAttribute("postedData",postedData);
		 return "redirect:/api/home";
	}
	
	@GetMapping("/api/home")
	public String displayHome(Model model) {
	    List<Post> latestAllPosts = service.findAll();
	    model.addAttribute("latestAllPosts", latestAllPosts);  // ← これがあればhome.htmlで使える
	    return "home";
	}
	
	@GetMapping("/api/delete/{id}")
	public String deletePost(@PathVariable Long id) {
		service.delete(id);
	    return "redirect:/api/home";
	}
}
