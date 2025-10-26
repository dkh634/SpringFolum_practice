package jp.dkh634.springforum.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.dkh634.springforum.repository.UserRepository;

@Component("dataBaseAuthenticator")
public class DataBaseAuthenticator implements Authenticator{

	@Autowired
	UserRepository userRepository;
	
	@Override
	public boolean authenticate(String username, String password) {
		// ユーザーに紐づくパスワードを取得(一旦平文で取得する。Todo passwordをhash化)
		Optional<String> DBpassword = userRepository.findPasswordByUsername(username);
		
		if(DBpassword.isEmpty()) {
			return false;
		}
		
		if(DBpassword.get().equals(password)) {
			return true;
		}
		return false;
	}

}
