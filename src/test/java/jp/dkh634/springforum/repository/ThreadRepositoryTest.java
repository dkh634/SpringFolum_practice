package jp.dkh634.springforum.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import jp.dkh634.springforum.entity.ForumThread;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test") // application-test.yml を使う（後述）
public class ThreadRepositoryTest {

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void resetSequence() {
        Long maxId = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(thread_id), 1) FROM thread", Long.class);
        jdbcTemplate.execute("SELECT setval('thread_thread_id_seq', " + maxId + ", true)");
    }

    @Test
    void タイトルが取得できる() {
        // データ準備
        ForumThread thread = new ForumThread();
        thread.setTitle("テストタイトル1");
        thread.setCreatedAt(LocalDateTime.of(2025,1,1,12,0));
        thread.setDeleted(false);
        thread = threadRepository.save(thread);

        // findTitleメソッドの動作確認
        String title = threadRepository.findTitle(thread.getThreadId());

        // Assert
        assertEquals("テストタイトル1", title);
    }
    

    @Test
    void 保存する() {
    	// データ準備
        ForumThread thread = new ForumThread();
        thread.setThreadId(1L);
        thread.setTitle("テストタイトル1");
        thread.setCreatedAt(LocalDateTime.of(2025,1,1,12,0));
        thread.setDeleted(false);
        
        threadRepository.saveForum(thread.getThreadId(),thread.getTitle(),thread.isDeleted());
        
     // 検証：JDBCを使って直接確認する
        String actualTitle = jdbcTemplate.queryForObject(
            "SELECT title FROM thread WHERE thread_id = ?", String.class, 1L);

        assertEquals("テストタイトル1", actualTitle);
    }
    
    @Test
    void 全件検索() {
    	// データ準備
        ForumThread thread1 = new ForumThread();
        thread1.setThreadId(1L);
        thread1.setTitle("テストタイトル1");
        thread1.setCreatedAt(LocalDateTime.of(2025,1,1,12,0));
        thread1.setDeleted(false);
        jdbcTemplate.update(
    		"INSERT INTO thread (thread_id, title, created_at,deleted) VALUES (?, ?, CURRENT_TIMESTAMP, ?) ",
    		thread1.getThreadId(),
    		thread1.getTitle(),
    		thread1.isDeleted()
        );
        
    	// データ準備
        ForumThread thread2 = new ForumThread();
        thread2.setThreadId(2L);
        thread2.setTitle("テストタイトル2");
        thread2.setCreatedAt(LocalDateTime.of(2025,1,1,12,0));
        thread2.setDeleted(false);
        jdbcTemplate.update(
    		"INSERT INTO thread (thread_id, title, created_at,deleted) VALUES (?, ?, CURRENT_TIMESTAMP, ?) ",
    		thread2.getThreadId(),
    		thread2.getTitle(),
    		thread2.isDeleted()
		);
        
    	// データ準備
        ForumThread thread3 = new ForumThread();
        thread3.setThreadId(3L);
        thread3.setTitle("テストタイトル1");
        thread3.setCreatedAt(LocalDateTime.of(2025,1,1,12,0));
        thread3.setDeleted(false);
        jdbcTemplate.update(
    		"INSERT INTO thread (thread_id, title, created_at,deleted) VALUES (?, ?, CURRENT_TIMESTAMP, ?) ",
    		thread3.getThreadId(),
    		thread3.getTitle(),
    		thread3.isDeleted()
        );
     
        
        //act
        List<ForumThread> forumThreadList = threadRepository.findAllList();
        
        assertTrue(forumThreadList.stream()
        	.anyMatch(t -> t.getThreadId().equals(thread1.getThreadId())
        		&& t.getTitle().equals(thread1.getTitle())
        	    && t.isDeleted() == thread1.isDeleted()));

        assertTrue(forumThreadList.stream()
        	.anyMatch(t -> t.getThreadId().equals(thread2.getThreadId())
        	    && t.getTitle().equals(thread2.getTitle())
        	    && t.isDeleted() == thread2.isDeleted()));

        assertTrue(forumThreadList.stream()
    		.anyMatch(t -> t.getThreadId().equals(thread3.getThreadId())
				&& t.getTitle().equals(thread3.getTitle())
			    && t.isDeleted() == thread3.isDeleted()));
    }
    
    @Test
    void 最大のThreadIdを取得する() {
    	// データ準備
        ForumThread thread1 = new ForumThread();
        thread1.setThreadId(1L);
        thread1.setTitle("テストタイトル1");
        thread1.setCreatedAt(LocalDateTime.of(2025,1,1,12,0));
        thread1.setDeleted(false);
        jdbcTemplate.update(
    		"INSERT INTO thread (thread_id, title, created_at,deleted) VALUES (?, ?, CURRENT_TIMESTAMP, ?) ",
    		thread1.getThreadId(),
    		thread1.getTitle(),
    		thread1.isDeleted()
		);
        
    	// データ準備
        ForumThread thread2 = new ForumThread();
        thread2.setThreadId(2L);
        thread2.setTitle("テストタイトル2");
        thread2.setCreatedAt(LocalDateTime.of(2025,1,1,12,0));
        thread2.setDeleted(false);
        jdbcTemplate.update(
    		"INSERT INTO thread (thread_id, title, created_at,deleted) VALUES (?, ?, CURRENT_TIMESTAMP, ?) ",
    		thread2.getThreadId(),
    		thread2.getTitle(),
    		thread2.isDeleted()
		);
        
    	// データ準備
        ForumThread thread3 = new ForumThread();
        thread3.setThreadId(3L);
        thread3.setTitle("テストタイトル1");
        thread3.setCreatedAt(LocalDateTime.of(2025,1,1,12,0));
        thread3.setDeleted(true);
        jdbcTemplate.update(
    		"INSERT INTO thread (thread_id, title, created_at,deleted) VALUES (?, ?, CURRENT_TIMESTAMP, ?) ",
    		thread3.getThreadId(),
    		thread3.getTitle(),
    		thread3.isDeleted()
        );
        
        int maxThreadId = threadRepository.findMaxThreadId();
        
        assertEquals(3, maxThreadId);
     
    }
}
