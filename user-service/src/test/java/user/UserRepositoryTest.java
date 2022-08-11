package user;

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

    @BeforeEach
    public void init() {
        user = entityManager.find(User.class, 1);
//        firstRole = entityManager.find(Role.class, 1);
//        entityManager.persist(user);
//        entityManager.flush();
    }

    @Test
    public void changeRoleTest() {
        userRepository.changeRole(2L, 1L);
//        User foundUser = userRepository.findByUsername().orElse(new User());
        List<Role> roles = new ArrayList<>(user.getRoles());
        Assertions.assertEquals("NEW_ROLE", roles.get(0).getName());
    }
}
