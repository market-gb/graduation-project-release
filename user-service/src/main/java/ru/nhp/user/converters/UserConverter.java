package ru.nhp.user.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nhp.api.dto.user.UserDto;
import ru.nhp.user.entites.Role;
import ru.nhp.user.entites.User;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserConverter {

    public User dtoToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .enabled(userDto.getEnabled())
                .roles(userDto.getRoles().stream().map(Role::new).toList())
                .build();
    }

    public UserDto entityToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
