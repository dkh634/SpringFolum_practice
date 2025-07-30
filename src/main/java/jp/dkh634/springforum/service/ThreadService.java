package jp.dkh634.springforum.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dkh634.springforum.entity.ForumThread;
import jp.dkh634.springforum.form.ThreadPostForm;
import jp.dkh634.springforum.repository.ThreadRepository;

@Service
public class ThreadService {
	
	
	@Autowired
	ThreadRepository threadrepo;
	
	/*
	 * スレッドの一覧を取得する
	 */
	public List<ForumThread> findAllThread(){
		return threadrepo.findAllList();
	}
	
    /*
     * threadIdを元にタイトルを取得する
     */
	public String findTitle(Long threadId) {
		String title = "threadIdを取得できませんでした";
		try {
			title = threadrepo.findTitle(threadId);
		}catch(Exception e){
			return title;
		}
		return title;
	}
	
    /**
     * 投稿フォームのデータをもとにThreadエンティティを生成する。
     *
     * @param postForm 投稿フォームの入力データ
     * @return 生成されたPostエンティティ
     */
    public ForumThread toEntity(ThreadPostForm threadPostForm) {
        ForumThread forumThread = new ForumThread();
        
        Integer maxThreadId = threadrepo.findMaxThreadId();
        Long threadId = (maxThreadId != null) ? maxThreadId.longValue() : 0L;

        // threadIdの採番
        Long generatedThreadId = generateThreadId(threadId);

        // titleをFormから取得する;
        forumThread.setTitle(threadPostForm.getTitle());
    	
        // createdAt;
        forumThread.setCreatedAt(LocalDateTime.now());
        
        //threadIdを設定する
        forumThread.setThreadId(generatedThreadId);
        
        //deletedを設定する(論理削除フラグ)
        forumThread.setDeleted(false);
        
        return forumThread;
    }
    
    /**
     * 投稿エンティティをデータベースに保存する。
     * <p>
     * ここではネイティブSQLのupsert的な処理を実行する
     * {@code threadrepo.saveForum(...)}を呼び出す。
     * </p>
     *
     * @param forumThread 保存対象のPostエンティティ
     */
    public void saveToDateBase(ForumThread forumThread) {
    	threadrepo.saveForum(
    		forumThread.getThreadId(),
    		forumThread.getTitle(),
    		forumThread.isDeleted()
        );
    }
    
    /**
     * 新しいcontentIdを採番する。
     * <p>
     * 引数がnullの場合は1を返し、nullでなければ引数に1を足して返す。
     * </p>
     *
     * @param contentId 現在の最大contentId
     * @return 新しく採番されたcontentId
     */
    public Long generateThreadId(Long threadId) {
        if (threadId == null) {
        	threadId = 1L;
        } else {
        	threadId += 1L;
        }
        return threadId;
    }


	
}
