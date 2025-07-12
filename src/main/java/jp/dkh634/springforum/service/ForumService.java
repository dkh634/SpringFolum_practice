package jp.dkh634.springforum.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dkh634.springforum.entity.Post;
import jp.dkh634.springforum.form.ForumPostForm;
import jp.dkh634.springforum.repository.PostRepository;

@Service
public class ForumService {
	
	@Autowired
	PostRepository postrepo;
	
	public int countDataBase() {
		int count= (int) postrepo.count();
		System.out.println(count);
		return count;
	}
	
	/*
	 * Formからentityに詰め替える
	 */
	public Post toEntity(ForumPostForm postForm) {
		//Formをentityに詰め替える
		Post post = new Post();
		post.setAuthorName(postForm.getAuthorName());
		post.setContent(postForm.getContent());
		post.setCreatedAt(LocalDateTime.now());
		//Todo 更新時刻を呼び出す必要あり。
		post.setUpdatedAt(null);
		//Todo:タイトルをどうするかはのちに決める
		post.setTitle("test");
		
		return post;
	}
	
	/*
	 * DBに保存する
	 */
	public Post saveToDateBase(Post post) {
		Post postresult = postrepo.save(post);
		return postresult;
	}
	
	/*
	 * DBをすべて取得する
	 */
	public List<Post> findAll(){
		return postrepo.findAllByOrderByCreatedAtAsc();
	}
    

	/*
	 * idに紐づくコメントを削除する
	 */
	public void delete(Long id){
		Optional<Post> foundPost=postrepo.findById(id);
		if(foundPost.isPresent()) {
			postrepo.deleteCommentsByPostId(id);
			postrepo.deleteById(id);
		}
	}
	
	/*
	 * 全部
	 */
	
	
}
