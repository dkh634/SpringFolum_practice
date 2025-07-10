package jp.dkh634.springforum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.dkh634.springforum.entity.Post;
import jp.dkh634.springforum.form.ForumPostForm;
import jp.dkh634.springforum.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
	
	@Mock
	private PostRepository postRepositoryMock;
	
    @InjectMocks
    private ForumService service;
    
	
	@Test
	public void countDataBase_データが存在する_1件(){
		
		when(postRepositoryMock.count()).thenReturn((long) 1);
		
		long result = service.countDataBase();
		
		assertEquals(1,result);
	}
	
	@Test
	public void countDataBase_データが存在しない_ゼロが返る() {
		
		when(postRepositoryMock.count()).thenReturn((long) 0);
		
		long result = service.countDataBase();
		
		assertEquals(0,result);
	}
	
	@Test
	public void toEntity_変換データが存在する() {
		
		//Formのデータを入れる
		ForumPostForm postForm = new ForumPostForm();
		postForm.setAuthorName("123456789abcdefghij");
		postForm.setContent("asdfghjkl");
		
		//詰め替えるmethodを呼ぶ
		Post resultPost = service.toEntity(postForm);
		
		//想定値のEntity作成
		Post expectedPost = new Post();
		expectedPost.setAuthorName("123456789abcdefghij");
		expectedPost.setContent("asdfghjkl");
		expectedPost.setCreatedAt(LocalDateTime.now());
		//Todo：あとで削除します
		expectedPost.setUpdatedAt(null);
		//Todo：固定値から変動値に変更します
		expectedPost.setTitle("test");

		// 想定値と実際の値を比較する
        assertEquals(expectedPost.getAuthorName(), resultPost.getAuthorName());
        assertEquals(expectedPost.getContent(), resultPost.getContent());
        assertEquals(expectedPost.getTitle(), resultPost.getTitle());
        assertTrue(expectedPost.getCreatedAt().isAfter(resultPost.getCreatedAt().minusSeconds(3)));
        assertEquals(expectedPost.getUpdatedAt(), resultPost.getUpdatedAt());
	}
	
	@Test
	public void saveDateBase_データベースに保存する() {
		
		//空のPostEntity
		Post post = new Post();
		
		//想定値のEntity作成
		Post expectedPost = new Post();
		expectedPost.setAuthorName("123456789abcdefghij");
		expectedPost.setContent("asdfghjkl");
		expectedPost.setCreatedAt(LocalDateTime.now());
		//Todo：あとで削除します
		expectedPost.setUpdatedAt(null);
		//Todo：固定値から変動値に変更します
		expectedPost.setTitle("test");
		
		//savemethodが呼ばれると想定値のモックを返却する
		when(postRepositoryMock.save(any(Post.class))).thenReturn(expectedPost);
		
		Post postresult = service.saveToDateBase(post);
		
		// 想定値と実際の値を比較する
        assertEquals(expectedPost.getAuthorName(), postresult.getAuthorName());
        assertEquals(expectedPost.getContent(), postresult.getContent());
        assertEquals(expectedPost.getTitle(), postresult.getTitle());
        assertTrue(expectedPost.getCreatedAt().isAfter(postresult.getCreatedAt().minusSeconds(3)));
        assertEquals(expectedPost.getUpdatedAt(), postresult.getUpdatedAt());
	}

	 @Test
	    public void findAll_データが存在する_昇順で返る() {
	        // Arrange
	        Post post1 = new Post();
	        post1.setId(1L);
	        post1.setCreatedAt(LocalDateTime.of(2023, 1, 1, 10, 0));

	        Post post2 = new Post();
	        post2.setId(2L);
	        post2.setCreatedAt(LocalDateTime.of(2023, 1, 2, 10, 0));

	        List<Post> mockList = Arrays.asList(post1, post2);

	        when(postRepositoryMock.findAllByOrderByCreatedAtAsc()).thenReturn(mockList);

	        // Act
	        List<Post> result = service.findAll();

	        // Assert
	        assertEquals(2, result.size());
	        assertEquals(1L, result.get(0).getId());
	        assertEquals(2L, result.get(1).getId());
	    }

	    @Test
	    public void delete_該当データが存在する_コメントと投稿を削除する() {
	        // Arrange
	        Long id = 1L;
	        Post post = new Post();
	        post.setId(id);

	        when(postRepositoryMock.findById(id)).thenReturn(Optional.of(post));

	        // Act
	        service.delete(id);

	        // Assert
	        verify(postRepositoryMock).deleteCommentsByPostId(id);
	        verify(postRepositoryMock).deleteById(id);
	    }

	    @Test
	    public void delete_該当データが存在しない_何もしない() {
	        // Arrange
	        Long id = 1L;
	        when(postRepositoryMock.findById(id)).thenReturn(Optional.empty());

	        // Act
	        service.delete(id);

	        // Assert
	        verify(postRepositoryMock, never()).deleteCommentsByPostId(any());
	        verify(postRepositoryMock, never()).deleteById(any());
	    
	    }
}
