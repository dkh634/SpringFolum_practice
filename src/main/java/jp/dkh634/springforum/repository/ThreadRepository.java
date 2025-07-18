package jp.dkh634.springforum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jp.dkh634.springforum.entity.ForumThread;

/**
 * フォーラムのスレッド情報を操作するリポジトリインターフェース。
 * {@link JpaRepository}を継承し、基本的なCRUD操作を提供する。
 */
@Repository
public interface ThreadRepository extends JpaRepository<ForumThread, Long> {
    
    /**
     * 削除されていないスレッドを作成日時の昇順で全件取得する。
     * 論理削除が実装されている場合は、削除済みのデータを除外して返すことを想定している。
     *
     * @return 作成日時の古い順に並んだフォーラムスレッドのリスト
     */
    List<ForumThread> findAllByDeletedFalseOrderByCreatedAtAsc();
    
    /**
     * 指定したスレッドIDに対応するスレッドのタイトルを取得する。
     *
     * @param threadId スレッドのID
     * @return 該当スレッドのタイトル文字列
     */
    @Query(value = "SELECT title FROM thread WHERE thread_id = :threadId", nativeQuery = true)
    String findTitle(Long threadId);
}
