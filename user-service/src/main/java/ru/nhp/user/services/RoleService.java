package ru.nhp.user.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.user.repositories.AuthorityRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final AuthorityRepository authorityRepository;

    public List<String> findAllRoleNames() {
        try {
            return authorityRepository.findAllNames();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Роли не найдены");
        }
    }
}
