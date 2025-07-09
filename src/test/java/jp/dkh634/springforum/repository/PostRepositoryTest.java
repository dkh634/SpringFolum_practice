package jp.dkh634.springforum.repository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.dkh634.springforum.entity.Post;

@ExtendWith(MockitoExtension.class)
public class PostRepositoryTest {

    @Mock
    private PostRepository postRepositoryMock;

    @Test
    public void testFindById() {
        Post mockPost = new Post();
        mockPost.setId(1L);
        mockPost.setTitle("モックの投稿");
        mockPost.setContent("これはモックデータです");
        mockPost.setAuthorName("たけぞう");

        when(postRepositoryMock.findById(1L)).thenReturn(Optional.of(mockPost));

        Optional<Post> result = postRepositoryMock.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("モックの投稿", result.get().getTitle());
    }

    @Test
    public void testSave() {
        Post mockPost = new Post();
        mockPost.setId(2L);
        mockPost.setTitle("新規投稿");
        mockPost.setContent("保存テスト");
        mockPost.setAuthorName("たけぞう");

        when(postRepositoryMock.save(mockPost)).thenReturn(mockPost);
        when(postRepositoryMock.findById(2L)).thenReturn(Optional.of(mockPost));

        postRepositoryMock.save(mockPost);
        Optional<Post> result = postRepositoryMock.findById(2L);

        assertTrue(result.isPresent());
        assertEquals("新規投稿", result.get().getTitle());
    }
}
