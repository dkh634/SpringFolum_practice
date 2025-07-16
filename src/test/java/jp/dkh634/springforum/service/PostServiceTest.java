package jp.dkh634.springforum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.dkh634.springforum.entity.Post;
import jp.dkh634.springforum.form.ForumPostForm;
import jp.dkh634.springforum.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepositoryMock;

    @InjectMocks
    private PostService service;

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2024, 1, 1, 0, 0);

    @BeforeEach
    void setup() {
        // Optional: if PostService were to take Clock as constructor injection
    }

    @Test
    public void countDataBase_データが存在する_1件() {
        when(postRepositoryMock.count()).thenReturn(1L);
        int result = service.countDataBase();
        assertEquals(1, result);
    }

    @Test
    public void countDataBase_データが存在しない_ゼロが返る() {
        when(postRepositoryMock.count()).thenReturn(0L);
        int result = service.countDataBase();
        assertEquals(0, result);
    }

    @Test
    public void toEntity_変換データが存在する() {
        ForumPostForm postForm = new ForumPostForm();
        postForm.setAuthorName("123456789abcdefghij");
        postForm.setContent("asdfghjkl");
        Long threadId = 1L;

        when(postRepositoryMock.findMaxContentIdByThreadId(threadId)).thenReturn(1L);

        Post resultPost = service.toEntity(postForm, threadId);

        assertEquals("123456789abcdefghij", resultPost.getAuthorName());
        assertEquals("asdfghjkl", resultPost.getContent());
        assertEquals(2L, resultPost.getContentId());
        assertEquals(threadId, resultPost.getThreadId());
        assertFalse(resultPost.isDeleted());
    }

    @Test
    public void saveToDateBase_データベースに保存する() {
        Post post = new Post();
        post.setContentId(1L);
        post.setContent("test content");
        post.setAuthorName("たけぞう");
        post.setThreadId(100L);

        doNothing().when(postRepositoryMock).savePost(
                eq(1L), eq("test content"), eq("たけぞう"), eq(100L)
        );

        service.saveToDateBase(post);

        verify(postRepositoryMock).savePost(1L, "test content", "たけぞう", 100L);
    }

    @Test
    public void findAll_データが存在する_昇順で返る() {
        Post post1 = new Post();
        post1.setContentId(1L);
        post1.setCreatedAt(LocalDateTime.of(2023, 1, 1, 10, 0));

        Post post2 = new Post();
        post2.setContentId(2L);
        post2.setCreatedAt(LocalDateTime.of(2023, 1, 2, 10, 0));

        List<Post> mockList = Arrays.asList(post1, post2);

        when(postRepositoryMock.findAllByThreadIdAndNotDeleted(anyLong())).thenReturn(mockList);

        List<Post> result = service.findAll(1L);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getContentId());
        assertEquals(2L, result.get(1).getContentId());
    }

    @Test
    public void delete_該当データが存在する_論理削除される() {
        Long contentId = 1L;
        Long threadId = 10L;
        Post post = new Post();
        post.setContentId(contentId);
        post.setThreadId(threadId);

        when(postRepositoryMock.findByIds(contentId, threadId)).thenReturn(Optional.of(post));

        service.delete(contentId, threadId);

        verify(postRepositoryMock).logicallyDeleteById(contentId, threadId);
    }

    @Test
    public void delete_該当データが存在しない_何もしない() {
        Long contentId = 1L;
        Long threadId = 10L;
        when(postRepositoryMock.findByIds(contentId, threadId)).thenReturn(Optional.empty());

        service.delete(contentId, threadId);

        verify(postRepositoryMock, never()).logicallyDeleteById(anyLong(), anyLong());
    }

    @Test
    public void generateContentId_nullを渡すと1を返す() {
        assertEquals(1L, service.generateContentId(null));
    }

    @Test
    public void generateContentId_値を渡すとインクリメントされる() {
        assertEquals(11L, service.generateContentId(10L));
    }
}