package jp.dkh634.springforum.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
		
		//modelに格納する
		model.addAttribute("postedData",postedData);
		 return "redirect:/api/home";
	}
	
	@GetMapping("/api/home")
	public String displayHome(Model model) {
	    // 最新の投稿1件を取得する（例：投稿日時で並び替え）
	    List<Post> latestAllPosts = postrepo.findAllByOrderByCreatedAtAsc();
	    model.addAttribute("latestAllPosts", latestAllPosts);  // ← これがあればhome.htmlで使える
	    return "home";
	}
	
	@GetMapping("/api/delete/{id}")
	public String deletePost(@PathVariable Long id) {
		Optional<Post> foundPost=postrepo.findById(id);
		if(foundPost.isPresent()) {
			postrepo.deleteCommentsByPostId(id);
			postrepo.deleteById(id);
		}
	   
	      return "redirect:/api/home";
	}
}
