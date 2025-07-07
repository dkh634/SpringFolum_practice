package jp.dkh634.springforum.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jp.dkh634.springforum.entity.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
	
}
