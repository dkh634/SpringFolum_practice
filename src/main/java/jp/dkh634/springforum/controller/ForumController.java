package jp.dkh634.springforum.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jp.dkh634.springforum.entity.Post;
import jp.dkh634.springforum.form.ForumPostForm;
import jp.dkh634.springforum.repository.PostRepository;
import jp.dkh634.springforum.service.ForumService;

@Controller
public class ForumController {
	@Autowired
    private ForumService forumService;
	
	@Autowired
	PostRepository postrepo;

	@GetMapping("/api/home")
	public String displayHome() {
		return "home";
	}
	
	@PostMapping("/api/post")
	public String reservePost(@ModelAttribute ForumPostForm postForm,Model model) {
		
		//受け取れているかチェックTodo；後で消す
		System.out.println(postForm.getAuthorName());
		System.out.println(postForm.getContent());
		
		//DBの件数を取得
		int DBcount= (int) postrepo.count();
		System.out.println(DBcount);
		
		//Formをentityに詰め替える
		Post post = new Post();
		post.setAuthorName(postForm.getAuthorName());
		post.setContent(postForm.getContent());
		post.setCreatedAt(LocalDateTime.now());
		post.setUpdatedAt(null);
		//Todo:タイトルをどうするかはのちに決める
		post.setTitle("test");
		
		
		
		//DB処理を入れる
		Post postedData = postrepo.save(post);
		int newDBcount= (int) postrepo.count();
		System.out.print(newDBcount);
		
		//挿入できているか確認
		System.out.println(postedData);
		return "home";
	}
	
	
}
