package jp.dkh634.springforum.repository;

import org.springframework.data.repository.CrudRepository;

import jp.dkh634.springforum.entity.Post;

public interface PostRepository extends CrudRepository<Post, Integer> {
	
}
