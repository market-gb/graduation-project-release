package ru.nhp.user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
import ru.nhp.user.entites.User;
import ru.nhp.user.repositories.AuthorityRepository;
import ru.nhp.user.repositories.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public Page<User> searchUsers(Integer page, Integer pageSize) {
        return userRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return userRepository.findById(id);
    }

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
    public void changeRole(String roleName, Long userId) {
        if (roleName == null || userId == null) {
            throw new InvalidParamsException("Невалидные параметры");
        }
        Long roleId;
        try {
            roleId = authorityRepository.findByName(roleName).getId();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Ошибка поиска роли. Роль " + roleName + "не существует");
        }
        try {
            userRepository.changeRole(roleId, userId);
            userRepository.changeUpdateAt(userId);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Ошибка изменения роли. Пользователь " + userId + "не существует");
        }
    }

    @Transactional
    public void changeEmail(Long userId, String newEmail) {
        if (newEmail.isBlank()){
            throw new InvalidParamsException("Email не может быть пустым");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> (new ResourceNotFoundException("Пользователь с идентификатором " + userId + " не найден")));
        user.setEmail(newEmail);
    }

    @Transactional
    public void changePassword(Long userId, String password) {
        if (password.isBlank()){
            throw new InvalidParamsException("Пароль не может быть пустым");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> (new ResourceNotFoundException("Пользователь с идентификатором " + userId + " не найден")));
        user.setPassword(password);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new InvalidParamsException("Невалидный параметр идентификатор:" + null);
        }
        try {
            userRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Ошибка удаления пользователя. Пользователь " + id + "не существует");
        }
    }
}