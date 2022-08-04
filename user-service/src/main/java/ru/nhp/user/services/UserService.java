package ru.nhp.user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nhp.api.exceptions.InvalidParamsException;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.user.entites.Role;
import ru.nhp.user.repositories.UserRepository;
import ru.nhp.user.entites.User;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(String.format("User '%s' not found", username)));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Transactional
    public void changeEmail(String username, String newEmail) {
        if (newEmail.isBlank()){
            throw new InvalidParamsException("Email не может быть пустым");
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> (new ResourceNotFoundException("Username is wrong")));
        user.setEmail(newEmail);
    }

    @Transactional
    public void changePassword(String username, String password) {
        if (password.isBlank()){
            throw new InvalidParamsException("Пароль не может быть пустым");
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> (new ResourceNotFoundException("Username is wrong")));
        user.setPassword(password);
    }
}