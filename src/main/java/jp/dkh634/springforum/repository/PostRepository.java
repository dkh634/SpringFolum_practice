package jp.dkh634.springforum.repository;

import java.util.List;

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
    @Query(value = "UPDATE post SET deleted = true WHERE id = :id", nativeQuery = true)
    void logicallyDeleteById(@Param("id") Long id);
    
    // 論理削除されていないデータだけ取得
    List<Post> findAllByDeletedFalseOrderByCreatedAtAsc();
    
   
}

