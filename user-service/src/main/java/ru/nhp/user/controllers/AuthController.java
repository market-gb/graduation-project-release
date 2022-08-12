package ru.nhp.user.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.nhp.api.exceptions.*;
import ru.nhp.user.dto.JwtRequest;
import ru.nhp.user.dto.JwtResponse;
import ru.nhp.user.entites.RegistrationToken;
import ru.nhp.user.services.RegisterService;
import ru.nhp.user.services.UserService;
import ru.nhp.user.utils.JwtTokenUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequiredArgsConstructor
@Tag(name = "Авторизация", description = "Методы работы с авторизацией")
public class AuthController {

    private final RegisterService registerService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token, jwtTokenUtil.getRoles(token)));
    }

    @PostMapping("/new_user_auth")
    public ResponseEntity<?> register(@RequestBody JwtRequest authRequest) {
        if (!validate(authRequest.getEmail()) || userService.findByEmail(authRequest.getEmail()).isPresent()) {
            throw new InvalidParamsException("Incorrect Email");
        }
        if (authRequest.getPassword().equals(authRequest.getPasswordConfirm())
                && userService.findByUsername(authRequest.getUsername()).isEmpty()) {
            registerService.signUp(authRequest.getUsername(), authRequest.getPassword(),
                    authRequest.getEmail());
            throw new WaitingConfirmException("Waiting for Email confirmation");
        }
        throw new BadCredentialsException("Incorrect username or password");
    }

    @GetMapping("/register")
    public String registerConfirm(@RequestParam String token) {
        if (registerService.confirmRegistration(token)) {
            token = registerService.updateToken(token);
            return "Поздравляем с успешной регистрацией! Можете войти под своим логином.";
        } else {
            RegistrationToken registrationToken = registerService.findRegistrationTokenByToken(token);
            if (registrationToken == null)
                throw new IncorrectTokenException("Incorrect token");
            registerService.resendingToken(registrationToken);
            throw new SecondConfirmationException("You did not have time to confirm the registration, " +
                    "a second confirmation email was sent to you");
        }
    }

}
