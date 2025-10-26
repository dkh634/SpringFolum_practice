package jp.dkh634.springforum.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.dkh634.springforum.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>{

	@Query(value = "SELECT password FROM users WHERE username = :username AND delete_flag = false", nativeQuery = true)
	Optional<String> findPasswordByUsername(@Param("username") String username);

}
