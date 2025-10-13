package jp.dkh634.springforum.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jp.dkh634.springforum.entity.ForumThread;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class ThreadRepositoryTest {

    // PostgreSQLコンテナ定義（testcontainerで自動起動）
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("forum_test")
            .withUsername("forumuser")
            .withPassword("forumpass");

    // SpringのDataSource設定を上書き
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
	
    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void createTable(@Autowired JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS thread (
                thread_id BIGSERIAL PRIMARY KEY,
                title VARCHAR(255),
                created_at TIMESTAMP,
                deleted BOOLEAN
            )
        """);
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
