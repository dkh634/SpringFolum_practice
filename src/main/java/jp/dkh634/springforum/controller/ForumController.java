package jp.dkh634.springforum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jp.dkh634.springforum.entity.ForumThread;
import jp.dkh634.springforum.entity.Post;
import jp.dkh634.springforum.form.ForumPostForm;
import jp.dkh634.springforum.service.PostService;
import jp.dkh634.springforum.service.ThreadService;

@Controller
public class ForumController {
	@Autowired
    private PostService postservice;
	
	@Autowired
	private ThreadService threadservice;
	
	/*
	 * 投稿したコメントをDBに保存する
	 */
	@PostMapping("/api/post")
	public String reservePost(@ModelAttribute ForumPostForm postForm, HttpSession session) {
	    Long threadId = (Long) session.getAttribute("threadId");
	    if (threadId == null) {
	        throw new IllegalStateException("threadId is missing in session");
	    }

	    Post post = postservice.toEntity(postForm, threadId);
	    postservice.saveToDateBase(post);

	    return "redirect:/api/thread/" + threadId;
	}

	
	/*
	 * 最新のコメントをDBから取得する
	 */
	@GetMapping("/api/thread/{id}")
	public String displayHome(Model model,@PathVariable("id") Long threadId,HttpSession session) {
	    List<Post> latestAllPosts = postservice.findAll(threadId);
	    model.addAttribute("latestAllPosts", latestAllPosts); 
	    session.setAttribute("threadId", threadId);
	    return "/thread";
	}
	
	/*
	 * 指定したIDに紐づく投稿コメントをDBから削除する(論理削除)
	 */
	@GetMapping("/api/delete/{id}")
	public String deletePost(@PathVariable Long id, HttpSession session) {
	    Long threadId = (Long) session.getAttribute("threadId");
	    if (threadId == null) {
	        throw new IllegalStateException("スレッドIDが見つかりません");
	    }
	    postservice.delete(id,threadId);
	    return "redirect:/api/thread/" + threadId;
	}

	/*
	 * スレッド一覧を取得してforumへ遷移
	 */
	@GetMapping("/api/forum")
	public String displayForum(Model model){
		 List<ForumThread> latestAllThread = threadservice.findAllThread();
		 model.addAttribute("latestAllThread", latestAllThread); 
		return "/forum";
	}
	
	
	
	
}
