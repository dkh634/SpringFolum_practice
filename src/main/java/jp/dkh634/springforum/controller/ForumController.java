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
	
    /**
     * 掲示板のスレッド一覧を取得し、掲示板一覧画面(forum.html)に遷移します。
     *
     * @param model モデルにスレッド一覧を追加します
     * @return 掲示板一覧画面(forum.html）へ遷移
     */
	@GetMapping("/api/forum")
	public String displayForum(Model model){
		 List<ForumThread> latestAllThread = threadservice.findAllThread();
		 model.addAttribute("latestAllThread", latestAllThread); 
		return "/forum";
	}
	
    /**
     * スレッドIDに紐づく投稿一覧を取得し、掲示板画面（thread.html）に遷移します。
     *
     * @param model コメント一覧を追加
     * @param threadId スレッドID
     * @param session スレッドIDを保持
     * @return 掲示板画面（thread.html）へ遷移
     */
	@GetMapping("/api/thread/{id}")
	public String displayHome(Model model,@PathVariable("id") Long threadId,HttpSession session) {
		
		//スレッドIDに紐づく投稿一覧を取得する
	    List<Post> latestAllPosts = postservice.findAll(threadId);
	    
	    //投稿一覧をrequestscopeに保存する
	    model.addAttribute("latestAllPosts", latestAllPosts); 
	    session.setAttribute("threadId", threadId);
	    
	    //
	    String title = threadservice.findTitle(threadId);
	    model.addAttribute("title",title);
	    return "/thread";
	}
	
    /**
     * 掲示板への投稿内容データをDBに保存し、対応するスレッドページにリダイレクトします。
     *
     * @param postForm 掲示板画面からの投稿内容のデータ {@ForumPostForm}
     * @param session スレッドIDを保持 
     * @return 掲示板画面(displayHome)へのリダイレクトURL
     * @throws IllegalStateException セッションにthreadIdが存在しない場合
     */
	@PostMapping("/api/post")
	public String reservePost(@ModelAttribute ForumPostForm postForm, HttpSession session) {
	    Long threadId = (Long) session.getAttribute("threadId");
	    if (threadId == null) {
	        throw new IllegalStateException("threadId is missing in session");
	    }

	    //FormをEntityに変換する
	    Post post = postservice.toEntity(postForm, threadId);
	    
	    //投稿内容をDBに保存する
	    postservice.saveToDateBase(post);

	    return "redirect:/api/thread/" + threadId;
	}
	
    /**
     * 掲示板の指定コメントを論理削除し、掲示板画面にリダイレクトします。
     *
     * @param id 削除対象の投稿ID
     * @param session スレッドIDを保持
     * @return 掲示板画面(displayHome)へのリダイレクトURL
     * @throws IllegalStateException セッションにthreadIdが存在しない場合
     */
	@GetMapping("/api/delete/{postId}")
	public String deletePost(@PathVariable Long postId, HttpSession session) {
	    Long threadId = (Long) session.getAttribute("threadId");
	    if (threadId == null) {
	        throw new IllegalStateException("スレッドIDが見つかりません");
	    }
	    postservice.delete(postId,threadId);
	    return "redirect:/api/thread/" + threadId;
	}


	
	
	
	
}
