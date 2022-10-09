package ru.nhp.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.nhp.user.entites.User;
import ru.nhp.user.repositories.UserRepository;

import java.time.LocalDateTime;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private LocalDateTime time;

    @BeforeEach
    public void init() {
        user = entityManager.find(User.class, 1L);
        user.setUpdatedAt(LocalDateTime.MIN);
        time = user.getUpdatedAt();
    }

    @Test
    public void changeUpdateAtTest() {
        userRepository.changeUpdateAt(1L);
        User user = entityManager.find(User.class, 1L);
        Assertions.assertNotEquals(time, user.getUpdatedAt());
    }
}
