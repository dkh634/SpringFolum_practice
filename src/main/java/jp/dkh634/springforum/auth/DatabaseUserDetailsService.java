package jp.dkh634.springforum.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.dkh634.springforum.repository.UserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 後述

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // DBからパスワード取得
        Optional<String> dbPasswordOpt = userRepository.findPasswordByUsername(username);

        if (dbPasswordOpt.isEmpty()) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + username);
        }
        
        // Spring Security に渡す UserDetails オブジェクトを作成
        return User.builder()
                .username(username)
                .password(dbPasswordOpt.get())  // ハッシュ化済みパスワードが理想
                .roles("USER")         // 固定ロールでもOK。将来的にDBに権限列を持たせても良い
                .build();
    }
}
