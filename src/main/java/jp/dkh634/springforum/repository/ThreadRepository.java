package jp.dkh634.springforum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.dkh634.springforum.entity.ForumThread;

@Repository
public interface ThreadRepository  extends JpaRepository<ForumThread, Long> {
    
    //論理削除されていないデータだけ取得
	List<ForumThread> findAllByOrderByCreatedAtAsc();
	
	/*
     * タイトルを取得する
     */
    @Query(value = "SELECT title FROM thread WHERE thread_id = :threadId", nativeQuery = true)
	String findTitle(Long threadId);
}
