package ru.nhp.user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.user.entites.RegistrationToken;
import ru.nhp.user.repositories.AuthorityRepository;
import ru.nhp.user.repositories.RegistrationTokenRepository;
import ru.nhp.user.repositories.UserRepository;
import ru.nhp.user.entites.User;
import ru.nhp.user.utils.JwtTokenUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static ru.nhp.user.enums.EmailType.USER_REGISTRATION;


@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthorityRepository authorityRepository;
    private final RegistrationTokenRepository registrationTokenRepository;
    private final EmailService emailService;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public void signUp(String username, String password, String email) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEnabled(false);
        user.setRoles(Set.of(authorityRepository.findByName("ROLE_USER")));
        userRepository.save(user);

        String tokenUid = UUID.randomUUID().toString();
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
        String tokenUid = UUID.randomUUID().toString();
        registrationTokenRepository.save(new RegistrationToken(tokenUid, LocalDateTime.now().plusMinutes(15), user));

        emailService.sendMail(USER_REGISTRATION, Map.of("token", tokenUid), List.of(user.getEmail()));
    }

    public RegistrationToken findRegistrationTokenByToken(String token) {
        return registrationTokenRepository.findRegistrationTokenByToken(token);
    }

    @Transactional
    public String updateToken(String token) {
        UserDetails userDetails = userService
                .loadUserByUsername(registrationTokenRepository.findUserByToken(token)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                        .getUsername());
        RegistrationToken registrationToken = registrationTokenRepository.findRegistrationTokenByToken(token);
        registrationToken.setToken(jwtTokenUtil.generateToken(userDetails));
        return registrationToken.getToken();
    }
}
