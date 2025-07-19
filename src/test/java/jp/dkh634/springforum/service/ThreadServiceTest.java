package jp.dkh634.springforum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.dkh634.springforum.entity.ForumThread;
import jp.dkh634.springforum.repository.ThreadRepository;

@ExtendWith(MockitoExtension.class)
public class ThreadServiceTest {

    @Mock
    private ThreadRepository threadRepository;

    @InjectMocks
    private ThreadService threadService;

    @Test
    void スレッド一覧を取得できる() {
        ForumThread thread1 = new ForumThread();
        thread1.setTitle("スレッド1");

        ForumThread thread2 = new ForumThread();
        thread2.setTitle("スレッド2");

        when(threadRepository.findAllByOrderByCreatedAtAsc()).thenReturn(Arrays.asList(thread1, thread2));

        List<ForumThread> result = threadService.findAllThread();

        assertEquals(2, result.size());
        assertEquals("スレッド1", result.get(0).getTitle());
    }

    @Test
    void タイトルを取得できる() {
        when(threadRepository.findTitle(1L)).thenReturn("テストタイトル");

        String title = threadService.findTitle(1L);

        assertEquals("テストタイトル", title);
    }

    @Test
    void タイトル取得時に例外が発生したら空文字が返る() {
        when(threadRepository.findTitle(999L)).thenThrow(new RuntimeException("DB error"));

        String title = threadService.findTitle(999L);

        assertEquals("", title);
    }
}
