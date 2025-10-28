package jp.dkh634.springforum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jp.dkh634.springforum.entity.Group;
import jp.dkh634.springforum.entity.Users;
import jp.dkh634.springforum.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService; // テスト対象

    @Test
    void getAllUsersが呼ばれること() {
        // --- グループ作成 ---
        Group testGroup1 = new Group();
        testGroup1.setId(1L);
        testGroup1.setName("testgroup1");
        testGroup1.setUsers(new HashSet<>()); // usersを空で初期化（Lombok の @Data による equals/hashCode が null で重複判定される問題を回避）

        Group testGroup2 = new Group();
        testGroup2.setId(2L);
        testGroup2.setName("testgroup2");
        testGroup2.setUsers(new HashSet<>()); //  usersを空で初期化（Lombok の @Data による equals/hashCode が null で重複判定される問題を回避）

        // --- ユーザー作成 ---
        Users testUser = new Users();
        testUser.setId(1L);
        testUser.setUsername("testuser1");
        testUser.setPassword("pass1");

        // HashSetに変換して groups にセット
        testUser.setGroups(new HashSet<>(List.of(testGroup1, testGroup2)));

        // --- モック定義 ---
        List<Users> userList = new ArrayList<>();
        userList.add(testUser);
        when(userRepository.findAllUsers()).thenReturn(userList);

        // --- 実行 ---
        List<Users> result = userService.getAllusers();
        
//        Set<Group> groups = result.get(0).getGroups();
//        System.out.println(groups.size()); // 2 になっているか確認
//        groups.forEach(g -> System.out.println(g.getId() + ": " + g.getName()));


        // --- 検証 ---
        assertNotNull(result);
        assertEquals(1, result.size());

//        // groupの中身を検証
//        Set<String> expectedGroupNames = Set.of("testgroup1", "testgroup2");
//        Set<String> actualGroupNames = result.get(0).getGroups()
//                                             .stream()
//                                             .map(Group::getName)
//                                             .collect(Collectors.toSet());
//        assertEquals(expectedGroupNames, actualGroupNames);
//
//        // 呼び出し検証
        verify(userRepository, times(1)).findAllUsers();
        

    }
}
