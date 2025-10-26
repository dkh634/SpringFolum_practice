package jp.dkh634.springforum.util;

import org.springframework.stereotype.Component;

@Component("devAuthenticator")
public class StubAuthenticator implements Authenticator {

    /**
     * ユーザー認証を行う
     *
     * @param username ユーザー名
     * @param password パスワード
     * @return 認証に成功した場合は true、それ以外は false
     */
	@Override
	public boolean authenticate(String username, String password) {
		if("username".equals(username) && "password".equals(password)) {
			return true;
		}
		return false;
	}
}
