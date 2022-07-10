package user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import user.controllers.AuthController;
import user.dto.JwtRequest;
import user.entites.Role;
import user.entites.User;
import user.services.UserService;
import user.utils.JwtTokenUtil;


import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private AuthenticationManager authenticationManager;

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0X3VzZXJuYW1lIiwicm9sZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJleHAiOjE2NTcyMjc4MzQsImlhdCI6MTY1NzE5MTgzNH0.gexPiJr4bQDvTxs9HcmOk2qrSmyYiBYqHE3Tg3vjLo0";
    private static final String USERNAME = "test_username";
    private static final String PASSWORD = "test_password";
    private static final String EMAIL = "test_email";
    private static final String ROLE_NAME = "test_role";
    private static UserDetails userDetails;
    private static JwtRequest jwtRequest;
    private static UsernamePasswordAuthenticationToken authenticationToken;


    @BeforeAll
    public static void initEntities() {
        Role role = new Role();
        role.setId(1L);
        role.setName(ROLE_NAME);

        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setRoles(List.of(role));

        userDetails = new org.springframework.security.core.userdetails.User(USERNAME,
                PASSWORD, List.of(new SimpleGrantedAuthority(ROLE_NAME)));

        jwtRequest = new JwtRequest();
        jwtRequest.setUsername(USERNAME);
        jwtRequest.setPassword(PASSWORD);

        authenticationToken = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
    }

    @Test
    public void createAuthTokenTest() throws Exception {
        given(userService.loadUserByUsername(USERNAME)).willReturn(userDetails);
        given(jwtTokenUtil.generateToken(userDetails)).willReturn(TOKEN);
        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD))).willReturn(authenticationToken);
        mvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(jwtRequest
                                )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(TOKEN)));
    }
}
