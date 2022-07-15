package user.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.entites.RegistrationToken;
import user.entites.User;
import user.repositories.AuthorityRepository;
import user.repositories.RegistrationTokenRepository;
import user.repositories.UserRepository;
import user.utils.JwtTokenUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static user.enums.EmailType.USER_REGISTRATION;


@Service
public class RegisterService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthorityRepository authorityRepository;
    private final RegistrationTokenRepository registrationTokenRepository;
    private final EmailService emailService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public RegisterService(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           AuthorityRepository authorityRepository,
                           RegistrationTokenRepository registrationTokenRepository,
                           EmailService emailService,
                           UserService userService,
                           JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authorityRepository = authorityRepository;
        this.registrationTokenRepository = registrationTokenRepository;
        this.emailService = emailService;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Transactional
    public void signUp(String username, String password, String email, String adress) {
        User user = new User();
        user.setEmail(email);
        user.setAddress(adress);
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEnabled(false);
        user.setRoles(Set.of(authorityRepository.findByName("ROLE_USER")));
        userRepository.save(user);

        UserDetails userDetails = userService.loadUserByUsername(username);
        String tokenUid = jwtTokenUtil.generateToken(userDetails);

        registrationTokenRepository.save(new RegistrationToken(tokenUid, LocalDateTime.now().plusMinutes(15), user));

        emailService.sendMail(USER_REGISTRATION, Map.of("token", tokenUid), List.of(email));
    }

    @Transactional
    public boolean confirmRegistration(String token) {
        Optional<User> user = registrationTokenRepository.findUserByTimeAndToken(LocalDateTime.now(), token);
        if (user.isEmpty()) {
            return false;
        }
        user.ifPresent(u -> u.setEnabled(true));
        return true;
    }

    public void resendingToken(RegistrationToken registrationToken) {
        User user = registrationToken.getUser();
        registrationTokenRepository.delete(registrationToken);
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        String tokenUid = jwtTokenUtil.generateToken(userDetails);
        registrationTokenRepository.save(new RegistrationToken(tokenUid, LocalDateTime.now().plusMinutes(15), user));

        emailService.sendMail(USER_REGISTRATION, Map.of("token", tokenUid), List.of(user.getEmail()));
    }

    public RegistrationToken findRegistrationTokenByToken(String token) {
        return registrationTokenRepository.findRegistrationTokenByToken(token);
    }
}
