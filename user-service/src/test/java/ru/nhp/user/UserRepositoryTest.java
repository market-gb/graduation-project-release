package ru.nhp.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.nhp.user.repositories.UserRepository;
import ru.nhp.user.entites.Role;
import ru.nhp.user.entites.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
//@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
//    private static final String USERNAME = "test_username";
//    private static final String PASSWORD = "test_password";
//    private static final String EMAIL = "test_email";

//    private Role firstRole;
    private User user;
    private LocalDateTime time;

    @BeforeEach
    public void init() {
        user = entityManager.find(User.class, 1L);
        user.setUpdatedAt(LocalDateTime.MIN);
        time = user.getUpdatedAt();
    }

    @Test
    public void changeRoleTest() {
        userRepository.changeRole(2L, 1L);
        User foundUser = entityManager.find(User.class, 1L);
        List<Role> roles = new ArrayList<>(foundUser.getRoles());
        Assertions.assertEquals("NEW_ROLE", roles.get(0).getName());
    }

    @Test
    public void changeUpdateAtTest(){
        userRepository.changeUpdateAt(1L);
        User user = entityManager.find(User.class, 1L);
        Assertions.assertNotEquals(time, user.getUpdatedAt());
    }
}
