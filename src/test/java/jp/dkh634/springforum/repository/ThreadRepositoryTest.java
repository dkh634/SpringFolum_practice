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

    private ForumThread thread1;
    private ForumThread thread2;
    
    @BeforeEach
    void resetSequence() {
        Long maxId = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(thread_id), 1) FROM thread", Long.class);
        jdbcTemplate.execute("SELECT setval('thread_thread_id_seq', " + maxId + ", true)");
        
    	//データ準備
    	thread1 = new ForumThread();
    	thread1.setTitle("テストタイトル1");
    	thread1.setCreatedAt(LocalDateTime.of(2025,1,1,12,0));
    	thread1 = threadRepository.save(thread1);
    	
    	//データ準備
    	thread2 = new ForumThread();
    	thread2.setTitle("テストタイトル2");
    	thread2.setCreatedAt(LocalDateTime.of(2025,1,2,13,0));
    	thread2 = threadRepository.save(thread2);
    }

    @Test
    void threadIdに紐づくタイトルが取得できる() {

        // findTitleを実行
        String title = threadRepository.findTitle(thread1.getThreadId());

        // Assert
        assertEquals("テストタイトル1", title);
    }
    

    @Test
    void タイトル一覧を取得できる() {
    	 // findAllByOrderByCreatedAtAscを実行
    	List<ForumThread> titleList = threadRepository.findAllByOrderByCreatedAtAsc();

        // Assert
        assertEquals("テストタイトル1", titleList.get(0).getTitle());
        assertEquals("テストタイトル2", titleList.get(1).getTitle());
    }
}
