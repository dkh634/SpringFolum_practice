package jp.dkh634.springforum.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

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
        // ① 現在の最大IDを取得
        Long maxId = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(thread_id), 0) FROM thread", Long.class);

        // ② シーケンス値を最大IDにセット
        // PostgreSQL のシーケンス名は 'thread_thread_id_seq' なので調整してください
        jdbcTemplate.execute("SELECT setval('thread_thread_id_seq', " + maxId + ")");
    }
    
    @Test
    void タイトルが取得できる() {
        // Arrange（データ準備）
        ForumThread thread = new ForumThread();
        thread.setTitle("テストタイトル1");
        thread.setCreatedAt(LocalDateTime.of(2025,1,1,12,0));
        thread = threadRepository.save(thread);

        // Act
        String title = threadRepository.findTitle(thread.getThreadId());

        // Assert
        assertEquals("テストタイトル1", title);
    }
}
