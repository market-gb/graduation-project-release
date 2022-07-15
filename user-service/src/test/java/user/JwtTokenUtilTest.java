package user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.JwtHelper;
import user.utils.JwtTokenUtil;


import java.util.List;

@SpringBootTest(classes = JwtTokenUtil.class)
public class JwtTokenUtilTest {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private static final String USERNAME = "test_username";
    private static final String PASSWORD = "test_password";
    private static final String ROLE_NAME = "test_role";
    private static UserDetails userDetails;

    @BeforeAll
    public static void init() {
        userDetails = new org.springframework.security.core.userdetails.User(USERNAME,
                PASSWORD, List.of(new SimpleGrantedAuthority(ROLE_NAME)));
    }

    @Test
    public void generateTokenTest() {
        String newToken = jwtTokenUtil.generateToken(userDetails);
        Assertions.assertEquals("HS256", JwtHelper.headers(newToken).get("alg"));
        Assertions.assertEquals("JWT", JwtHelper.headers(newToken).get("typ"));
    }
}
