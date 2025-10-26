package jp.dkh634.springforum.util;

public interface Authenticator {
    
    /**
     * ユーザー認証を行う
     *
     * @param username ユーザー名
     * @param password パスワード
     * @return 認証に成功した場合は true、それ以外は false
     */
    public boolean authenticate(String username, String password) ;
}
