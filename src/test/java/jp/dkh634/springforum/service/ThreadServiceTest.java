package jp.dkh634.springforum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.dkh634.springforum.entity.ForumThread;
import jp.dkh634.springforum.form.ThreadForm;
import jp.dkh634.springforum.repository.ThreadRepository;

@ExtendWith(MockitoExtension.class)
public class ThreadServiceTest {
    @Mock
    private ThreadRepository threadRepositoryMock;

    @InjectMocks
    private ThreadService threadService;

    @Test
    void findAllThreadメソッドが呼べること() {
    	// モックの挙動を定義
        ForumThread mockThread = new ForumThread();
        when(threadRepositoryMock.findAllList()).thenReturn(List.of(mockThread));

        // メソッド呼び出し
        List<ForumThread> result = threadService.findAllThread();

        // 結果の検証
        assertEquals(1, result.size());
        assertSame(mockThread, result.get(0));

        // モックの呼び出し確認（任意）
        verify(threadRepositoryMock).findAllList();
    }
    
    @Test
    void findTitleメソッドを呼べること() {
    	Long threadId = 1L;
    	
    	when(threadRepositoryMock.findTitle(threadId)).thenReturn("タイトル");
    	
    	// メソッドの呼び出し
    	String result =threadService.findTitle(threadId);
    	
    	// 結果の検証
    	assertEquals(result,"タイトル");
    }
    
    @Test
    void findTitleメソッドを呼べないこと() {
    	Long threadId = 1L;
    	
    	when(threadRepositoryMock.findTitle(threadId)).thenThrow(new RuntimeException("threadIdを取得できませんでした"));
    	
    	// メソッドの呼び出し
    	String result =threadService.findTitle(threadId);
    	
    	// 結果の検証
    	assertEquals(result,"threadIdを取得できませんでした");
    }
    
    @Test
    void toEntityメソッドを呼べること() {
        // Arrange: モックの挙動と入力データを用意
        when(threadRepositoryMock.findMaxThreadId()).thenReturn(5); // 次は6

        ThreadForm form = new ThreadForm();
        form.setTitle("テストタイトル");

        // Act: メソッド呼び出し
        ForumThread result = threadService.toEntity(form);

        // Assert: 結果の検証
        assertEquals("テストタイトル", result.getTitle());
        assertEquals(6L, result.getThreadId());
        assertNotNull(result.getCreatedAt());
        assertFalse(result.isDeleted());

        // Repositoryメソッドが呼ばれたか検証（任意）
        verify(threadRepositoryMock).findMaxThreadId();
    }
    
    @Test
    void saveToDateBaseメソッドが正しく呼ばれること() {
        // Arrange
        ForumThread thread = new ForumThread();
        thread.setThreadId(10L);
        thread.setTitle("テストタイトル");
        thread.setDeleted(false);

        // Act
        threadService.saveToDateBase(thread);

        // Assert: モックの呼び出しを検証
        verify(threadRepositoryMock).saveForum(10L, "テストタイトル", false);
    }

    @Test
    void generateThreadIdでnullでないときは加算されること() {
        Long result = threadService.generateThreadId(5L);
        assertEquals(6L, result);
    }

    @Test
    void generateThreadIdでnullのときは1が返ること() {
        Long result = threadService.generateThreadId(null);
        assertEquals(1L, result);
    }
}
