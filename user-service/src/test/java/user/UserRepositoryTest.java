package user;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import user.entites.Role;
import user.entites.User;
import user.repositories.UserRepository;

import java.util.List;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    private static final String USERNAME = "test_username";
    private static final String PASSWORD = "test_password";
    private static final String EMAIL = "test_email";
    private static final String ROLE_NAME = "test_role";
    private Role role;

    @BeforeEach
    public void init() {
        role = new Role();
        role.setId(1L);
        role.setName(ROLE_NAME);

        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setRoles(List.of(role));
        entityManager.persist(user);
        entityManager.flush();
    }

//    @Test
//    public void findByUsernameTest() {
//        User foundUser = userRepository.findByUsername(USERNAME).orElse(new User());
//        Assertions.assertEquals(USERNAME, foundUser.getUsername());
//        Assertions.assertEquals(PASSWORD, foundUser.getPassword());
//        Assertions.assertEquals(EMAIL, foundUser.getEmail());
//        Assertions.assertEquals(List.of(role), foundUser.getRoles());
//    }
}
