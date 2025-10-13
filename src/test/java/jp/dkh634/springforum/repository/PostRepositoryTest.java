package jp.dkh634.springforum.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import jp.dkh634.springforum.entity.Post;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.sql.init.mode=never")
public class PostRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("forum_test")
            .withUsername("forumuser")
            .withPassword("forumpass");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Post dummyPost;

    @BeforeAll
    static void setupDatabase(@Autowired JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS post (
                content_id BIGSERIAL PRIMARY KEY,
                thread_id BIGINT NOT NULL,
                content TEXT,
                author_name VARCHAR(255),
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                deleted BOOLEAN DEFAULT false
            )
        """);
    }

    @BeforeEach
    void setUp() {
        // テーブルをリセット
        jdbcTemplate.execute("TRUNCATE TABLE post RESTART IDENTITY");

        // ダミーデータ作成
        dummyPost = new Post();
        dummyPost.setThreadId(1L);
        dummyPost.setContent("テスト投稿");
        dummyPost.setAuthorName("たけぞう");
        dummyPost.setDeleted(false);

        // JdbcTemplateでINSERT
        String sql = "INSERT INTO post (thread_id, content, author_name, deleted) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, 
            dummyPost.getThreadId(), 
            dummyPost.getContent(), 
            dummyPost.getAuthorName(), 
            dummyPost.isDeleted()
        );
    }


    @Test
    void savePostメソッドが機能していること() {
        // テストデータをINSERT
        postRepository.savePost(2L, "テスト投稿2", "たけぞう2", 2L);

        // JdbcTemplateで直接SELECTして確認
        String sql = "SELECT content_id, thread_id, content, author_name, deleted FROM post WHERE content_id = ?";
        Post searchResult = jdbcTemplate.queryForObject(
            sql,
            new Object[]{2L}, // プレースホルダに値をセット
            (rs, rowNum) -> {
                Post p = new Post();
                p.setContentId(rs.getLong("content_id"));
                p.setThreadId(rs.getLong("thread_id"));
                p.setContent(rs.getString("content"));
                p.setAuthorName(rs.getString("author_name"));
                p.setDeleted(rs.getBoolean("deleted"));
                return p;
            }
        );

        assertNotNull(searchResult);
        assertEquals(2L, searchResult.getContentId());
        assertEquals(2L, searchResult.getThreadId()); // INSERT時の値に合わせる
        assertEquals("テスト投稿2", searchResult.getContent());
        assertEquals("たけぞう2", searchResult.getAuthorName());
        assertFalse(searchResult.isDeleted());
    }


    @Test
    void saveAndFindByIdsメソッドが機能していること() {
        Optional<Post> found = postRepository.findByIds(1L,1L);
        assertTrue(found.isPresent());
        assertEquals("テスト投稿", found.get().getContent());
    }

    @Test
    void logicallyDeleteByIdTestメソッドが機能していること() {
        // ダミーデータ作成
        dummyPost = new Post();
        dummyPost.setThreadId(3L);
        dummyPost.setContent("テスト投稿3");
        dummyPost.setAuthorName("たけぞう3");
        dummyPost.setDeleted(false);

        // JdbcTemplateでINSERT
        String insertSql = "INSERT INTO post (thread_id, content, author_name, deleted) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertSql, 
            dummyPost.getThreadId(), 
            dummyPost.getContent(), 
            dummyPost.getAuthorName(), 
            dummyPost.isDeleted()
        );

        // 挿入したレコードの content_id を取得
        Long contentId = jdbcTemplate.queryForObject(
            "SELECT content_id FROM post WHERE thread_id = ? AND content = ?",
            new Object[]{dummyPost.getThreadId(), dummyPost.getContent()},
            Long.class
        );

        // 論理削除
        postRepository.logicallyDeleteById(contentId, dummyPost.getThreadId());

        // 論理削除後にdeletedフラグを確認
        Boolean deleted = jdbcTemplate.queryForObject(
            "SELECT deleted FROM post WHERE content_id = ?",
            new Object[]{contentId},
            Boolean.class
        );

        // 論理削除になっていることを確認
        assertTrue(deleted);
    }


    @Test
    void findAllByThreadIdAndNotDeletedTestメソッドが機能していること() {
        // ダミーデータ作成
        dummyPost = new Post();
        dummyPost.setThreadId(4L);
        dummyPost.setContent("テスト投稿3");
        dummyPost.setAuthorName("たけぞう3");
        dummyPost.setDeleted(false);

        // JdbcTemplateでINSERT
        String insertSql = "INSERT INTO post (thread_id, content, author_name, deleted) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertSql, 
            dummyPost.getThreadId(), 
            dummyPost.getContent(), 
            dummyPost.getAuthorName(), 
            dummyPost.isDeleted()
        );
        
        // ダミーデータ作成
        dummyPost = new Post();
        dummyPost.setThreadId(4L);
        dummyPost.setContent("テスト投稿4");
        dummyPost.setAuthorName("たけぞう4");
        dummyPost.setDeleted(false);

        // JdbcTemplateでINSERT
        String insertSql2 = "INSERT INTO post (thread_id, content, author_name, deleted) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertSql2, 
            dummyPost.getThreadId(), 
            dummyPost.getContent(), 
            dummyPost.getAuthorName(), 
            dummyPost.isDeleted()
        );
        List<Post> posts = postRepository.findAllByThreadIdAndNotDeleted(4L);
        assertEquals(2, posts.size());
        assertEquals("テスト投稿3",posts.get(0).getContent());
        assertEquals("テスト投稿4",posts.get(1).getContent());
    }

    @Test
    void findMaxContentIdByThreadIdTestメソッドが機能していること() {

        // ダミーデータ作成
        dummyPost = new Post();
        dummyPost.setThreadId(5L);
        dummyPost.setContent("テスト投稿5-1");
        dummyPost.setAuthorName("たけぞう5-1");
        dummyPost.setDeleted(false);

        // JdbcTemplateでINSERT
        String insertSql = "INSERT INTO post (thread_id, content, author_name, deleted) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insertSql, 
            dummyPost.getThreadId(), 
            dummyPost.getContent(), 
            dummyPost.getAuthorName(), 
            dummyPost.isDeleted()
        );
        
        // ダミーデータ作成
        dummyPost = new Post();
        dummyPost.setThreadId(5L);
        dummyPost.setContent("テスト投稿5-2");
        dummyPost.setAuthorName("たけぞう5-2");
        dummyPost.setDeleted(false);
        
        Long maxId = postRepository.findMaxContentIdByThreadId(5L);
        assertEquals(2L, maxId);
    }
}
