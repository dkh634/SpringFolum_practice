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
public class PostService {
	
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
	public Post toEntity(ForumPostForm postForm,Long threadId) {
	    Post post = new Post();
	    post.setAuthorName(postForm.getAuthorName());
	    post.setContent(postForm.getContent());
	    post.setCreatedAt(LocalDateTime.now());
	    post.setDeleted(false);
	    //最新のThreadIDを
	    post.setThreadId(threadId); // ← 追加！
	    return post;
	}

	
	/*
	 * DBに保存する
	 */
	public Post saveToDateBase(Post post) {
	    // threadIdに紐づく最大contentIdを取得
	    Long maxContentId = postrepo.findMaxContentIdByThreadId(post.getThreadId());

	    if (maxContentId == null) {
	        maxContentId = 0L;
	    }
	    post.setContentId(maxContentId + 1L);

	    Post postresult = postrepo.save(post);
	    return postresult;
	}

	
	/*
	 * DBをすべて取得する
	 */
	public List<Post> findAll(Long threadId){
		return postrepo.findAllByThreadIdAndNotDeleted(threadId);
	}
    

	/*
	 * idに紐づくコメントを削除する
	 */
	public void delete(Long contentId,Long threadId){
		Optional<Post> foundPost=postrepo.findById(contentId);
		if(foundPost.isPresent()) {
			postrepo.logicallyDeleteById(contentId,threadId);
		}
	}
	
	/*
	 * content_idに紐づくPostテーブルのレコードを取得する
	 */
	

}
