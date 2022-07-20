package user.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import user.dto.JwtRequest;
import user.dto.JwtResponse;
import user.entites.RegistrationToken;
import user.exceptions.AppError;
import user.services.RegisterService;
import user.services.UserService;
import user.utils.JwtTokenUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequiredArgsConstructor
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
            return new ResponseEntity<>(new AppError("AUTH_SERVICE_INCORRECT_USERNAME_OR_PASSWORD", "Incorrect username or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/new_user_auth")
    public ResponseEntity<?> register(@RequestBody JwtRequest authRequest) {

        if (!validate(authRequest.getEmail()) || userService.findByEmail(authRequest.getEmail()).isPresent()) {
            return new ResponseEntity<>(new AppError("AUTH_SERVICE_INCORRECT_EMAIL",
                    "Incorrect Email"), HttpStatus.UNAUTHORIZED);
        }
        if (authRequest.getPassword().equals(authRequest.getPasswordConfirm())
                && userService.findByUsername(authRequest.getUsername()).isEmpty()) {
            registerService.signUp(authRequest.getUsername(), authRequest.getPassword(),
                    authRequest.getEmail(), authRequest.getAdress());
            return new ResponseEntity<>(
                    new AppError("AUTH_SERVICE_WAITING_FOR_EMAIL_CONFIRMATION",
                            "Waiting for Email confirmation"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(
                new AppError("AUTH_SERVICE_INCORRECT_USERNAME_OR_PASSWORD",
                        "Incorrect username or password"), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/register")
    public ResponseEntity<?> registerConfirm(@RequestParam String token) {
        if (registerService.confirmRegistration(token)) {
            token = registerService.updateToken(token);
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            RegistrationToken registrationToken = registerService.findRegistrationTokenByToken(token);
            if (registrationToken == null)
                return new ResponseEntity<>(new AppError("AUTH_SERVICE_INCORRECT_TOKEN",
                    "Incorrect token"), HttpStatus.UNAUTHORIZED);
            registerService.resendingToken(registrationToken);
            return new ResponseEntity<>(
                    new AppError("SECOND_CONFIRMATION_EMAIL",
                            "You did not have time to confirm the registration, " +
                                    "a second confirmation email was sent to you"), HttpStatus.UNAUTHORIZED);
        }
    }

}
