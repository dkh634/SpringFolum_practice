package jp.dkh634.springforum.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.dkh634.springforum.entity.Post;

@ExtendWith(MockitoExtension.class)
public class PostRepositoryTest {

    @Mock
    private PostRepository postRepository;

    private Post dummyPost;

    private static final LocalDateTime FIXED_DATE_TIME = LocalDateTime.of(2024, 1, 1, 12, 0);

    @BeforeEach
    void setUp() {
        dummyPost = new Post();
        dummyPost.setContentId(1L);
        dummyPost.setContent("テスト投稿");
        dummyPost.setCreatedAt(FIXED_DATE_TIME);
        dummyPost.setDeleted(false);
        dummyPost.setThreadId(100L);
        dummyPost.setAuthorName("たけぞう");
    }

    @Test
    void logicallyDeleteByIdTest() {
        doNothing().when(postRepository).logicallyDeleteById(1L, 100L);

        postRepository.logicallyDeleteById(1L, 100L);

        verify(postRepository, times(1)).logicallyDeleteById(1L, 100L);
    }

    @Test
    void findAllByThreadIdAndNotDeletedTest() {
        when(postRepository.findAllByThreadIdAndNotDeleted(100L)).thenReturn(List.of(dummyPost));

        List<Post> result = postRepository.findAllByThreadIdAndNotDeleted(100L);

        assertEquals(1, result.size());
        assertEquals("テスト投稿", result.get(0).getContent());
        assertFalse(result.get(0).isDeleted());
    }

    @Test
    void findMaxContentIdByThreadIdTest() {
        when(postRepository.findMaxContentIdByThreadId(100L)).thenReturn(42L);

        Long maxContentId = postRepository.findMaxContentIdByThreadId(100L);

        assertEquals(42L, maxContentId);
    }

    @Test
    void savePostTest() {
        doNothing().when(postRepository).savePost(
            1L, "テスト投稿", "たけぞう", 100L
        );

        postRepository.savePost(1L, "テスト投稿", "たけぞう", 100L);

        verify(postRepository).savePost(1L, "テスト投稿", "たけぞう", 100L);
    }

    @Test
    void findByIdsTest() {
        when(postRepository.findByIds(1L, 100L)).thenReturn(Optional.of(dummyPost));

        Optional<Post> result = postRepository.findByIds(1L, 100L);

        assertTrue(result.isPresent());
        assertEquals("テスト投稿", result.get().getContent());
    }
}
