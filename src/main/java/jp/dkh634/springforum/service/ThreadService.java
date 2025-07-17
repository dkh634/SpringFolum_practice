package jp.dkh634.springforum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dkh634.springforum.entity.ForumThread;
import jp.dkh634.springforum.repository.ThreadRepository;

@Service
public class ThreadService {
	
	
	@Autowired
	ThreadRepository threadrepo;
	
	/*
	 * スレッドの一覧を取得する
	 */
	public List<ForumThread> findAllThread(){
		return threadrepo.findAllByOrderByCreatedAtAsc();
	}
	
    /*
     * threadIdを元にタイトルを取得する
     */
	public String findTitle(Long threadId) {
		String title = "";
		try {
			title = threadrepo.findByTitle(threadId);
		}catch(Exception e){
			return title;
		}
		return title;
	}

	
}
