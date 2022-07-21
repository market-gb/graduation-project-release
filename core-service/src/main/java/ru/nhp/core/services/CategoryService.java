package ru.nhp.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nhp.core.entities.Category;
import ru.nhp.core.repositories.CategoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Optional<Category> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return categoryRepository.findById(id);
    }
}
