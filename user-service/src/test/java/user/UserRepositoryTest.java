package user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.nhp.user.repositories.UserRepository;
import ru.nhp.user.repositories.AuthorityRepository;
import ru.nhp.user.entites.Role;
import ru.nhp.user.entites.User;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private TestEntityManager entityManager;
    private static final String USERNAME = "test_username";
    private static final String PASSWORD = "test_password";
    private static final String EMAIL = "test_email";
    private static final String ROLE_NAME = "test_first_role";
    private static final String NEW_ROLE_NAME = "test_second_role";
    private Role firstRole;
    private Role secondRole;

    @BeforeEach
    public void init() {

        firstRole = new Role();
        firstRole.setId(1L);
        firstRole.setName(ROLE_NAME);

        secondRole = new Role();
        secondRole.setId(2L);
        secondRole.setName(NEW_ROLE_NAME);

        authorityRepository.saveAll(List.of(new Role[]{firstRole, secondRole}));

        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setEnabled(true);
        user.setRoles(List.of(firstRole));
        entityManager.persist(user);
        entityManager.flush();
    }

    @Test
    public void changeRoleTest() {
        userRepository.changeRole(2L, 1L);
        User foundUser = userRepository.findByUsername(USERNAME).orElse(new User());
        List<Role> roles = new ArrayList<>(foundUser.getRoles());
        Assertions.assertEquals(NEW_ROLE_NAME, roles.get(0).getName());
    }
}
