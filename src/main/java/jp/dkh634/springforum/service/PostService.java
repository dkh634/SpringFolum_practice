package jp.dkh634.springforum.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dkh634.springforum.entity.Post;
import jp.dkh634.springforum.form.ForumPostForm;
import jp.dkh634.springforum.repository.PostRepository;

/**
 * 投稿（Post）に関する業務ロジックを扱うサービスクラス。
 * 
 * ForumPostFormからPostエンティティへの変換や、
 * データベースへの保存、取得、論理削除などを提供する。
 * 
 */
@Service
public class PostService {

    @Autowired
    PostRepository postrepo;

    /**
     * データベースに保存されている投稿の件数を取得する。
     *
     * @return 投稿件数
     */
    public int countDataBase() {
        int count = (int) postrepo.count();
        System.out.println(count);
        return count;
    }

    /**
     * 投稿フォームのデータをもとにPostエンティティを生成する。
     * 
     * 引数のthreadIdに紐づく投稿の中で最大のcontentIdを取得し、
     * その値を元に新規contentIdを採番して設定する。
     * 現在日時を作成日時としてセットする。
     * 
     *
     * @param postForm 投稿フォームの入力データ
     * @param threadId 紐づくスレッドID
     * @return 生成されたPostエンティティ
     */
    public Post toEntity(ForumPostForm postForm, Long threadId) {
        Post post = new Post();

        // threadIdに紐づく最大contentIdを取得
        Long maxContentId = postrepo.findMaxContentIdByThreadId(threadId);

        // contentIdの採番
        Long generatedContentId = generateContentId(maxContentId);

        post.setContentId(generatedContentId);
        post.setAuthorName(postForm.getAuthorName());
        post.setContent(postForm.getContent());
        post.setCreatedAt(LocalDateTime.now());
        post.setDeleted(false);
        post.setThreadId(threadId);
        return post;
    }

    /**
     * 投稿エンティティをデータベースに保存する。
     * 
     * ここではネイティブSQLのupsert的な処理を実行する
     * {@code postrepo.savePost(...)}を呼び出す。
     * 
     *
     * @param post 保存対象のPostエンティティ
     */
    public void saveToDateBase(Post post) {
        postrepo.savePost(
            post.getContentId(),
            post.getContent(),
            post.getAuthorName(),
            post.getThreadId()
        );
    }

    /**
     * 指定されたスレッドIDに紐づく、
     * 削除されていない投稿一覧を取得する。
     *
     * @param threadId 取得対象のスレッドID
     * @return 削除されていない投稿のリスト
     */
    public List<Post> findAll(Long threadId) {
        return postrepo.findAllByThreadIdAndNotDeleted(threadId);
    }

    /**
     * 指定したcontentIdとthreadIdに該当する投稿を論理削除する。
     * 
     * 投稿が存在する場合に論理削除を行い、
     * 存在しない場合は処理を行わない。
     * 
     *
     * @param contentId 投稿のcontentId
     * @param threadId 投稿のthreadId
     */
    public void delete(Long contentId, Long threadId) {
    	//Todo　IDの採番がよくないのかな
        Optional<Post> foundPost = postrepo.findByIds(contentId,threadId);
        if (foundPost.isPresent()) {
            postrepo.logicallyDeleteById(contentId, threadId);
        }
    }

    /**
     * 新しいcontentIdを採番する。
     * 
     * 引数がnullの場合は1を返し、nullでなければ引数に1を足して返す。
     * 
     *
     * @param contentId 現在の最大contentId
     * @return 新しく採番されたcontentId
     */
    public Long generateContentId(Long contentId) {
        if (contentId == null) {
            contentId = 1L;
        } else {
            contentId += 1L;
        }
        return contentId;
    }
}
