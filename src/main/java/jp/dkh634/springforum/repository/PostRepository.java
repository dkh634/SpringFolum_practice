package jp.dkh634.springforum.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jp.dkh634.springforum.entity.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
	List<Post> findAllByOrderByCreatedAtAsc();
	
	@Modifying
    @Transactional
    @Query("DELETE FROM comments WHERE post_id = :postId")
    void deleteCommentsByPostId(@Param("postId") Long postId);
}
