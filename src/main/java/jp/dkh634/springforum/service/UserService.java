package jp.dkh634.springforum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dkh634.springforum.entity.Users;
import jp.dkh634.springforum.repository.UserRepository;

/**
 * User管理用のServiceクラス
 */
@Service
public class UserService {
	
	@Autowired
	UserRepository userrepo;
	
	/**
	 * すべてのUserを取得するクラス
	 * @return userList
	 */
	public List<Users> getAllusers() {
		List<Users> userList = new ArrayList<Users>();
		userList = userrepo.findAllUsers();
		return userList;
	}

}
