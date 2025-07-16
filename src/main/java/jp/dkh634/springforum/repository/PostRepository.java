package jp.dkh634.springforum.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jp.dkh634.springforum.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE post SET deleted = true WHERE content_id = :contentId and thread_id = :threadId", nativeQuery = true)
    void logicallyDeleteById(@Param("contentId") Long contentId,@Param("threadId") Long threadId );
    
    // 論理削除されておらず、引数で渡したthreadIdに紐づくレコードを取得する
    @Query(value = "SELECT * FROM post WHERE deleted = false AND thread_id = :threadId ORDER BY content_id ", nativeQuery = true)
    List<Post> findAllByThreadIdAndNotDeleted(@Param("threadId") Long threadId);

    // threadIdで最大contentIdを取得する（存在しなければnull）
    @Query(value = "SELECT MAX(content_id) FROM post WHERE thread_id = :threadId", nativeQuery = true)
    Long findMaxContentIdByThreadId(@Param("threadId") Long long1);
    
    
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO post (content_id, content, author_name, created_at, deleted, thread_id)
        VALUES (:contentId, :content, :authorName, CURRENT_TIMESTAMP, false, :threadId)
    """, nativeQuery = true)
    void savePost(
        @Param("contentId") Long contentId,
        @Param("content") String content,
        @Param("authorName") String authorName,
        @Param("threadId") Long threadId
    );
    
 // 論理削除されておらず、引数で渡したthreadIdに紐づくレコードを取得する
    @Query(value = "SELECT * FROM post WHERE deleted = false AND thread_id = :threadId and content_id = :contentId", nativeQuery = true)
    Optional<Post> findByIds(@Param("contentId") Long contentId,@Param("threadId") Long threadId );
}

