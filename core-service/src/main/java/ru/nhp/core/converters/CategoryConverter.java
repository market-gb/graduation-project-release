package ru.nhp.core.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.core.entities.Category;
import ru.nhp.core.services.CategoryService;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryConverter {
    private final CategoryService categoryService;

    public Set<Long> setEntitiesToSetId(Set<Category> categories) {
        return categories.stream().map(Category::getId).collect(Collectors.toSet());
    }

    public Set<Category> setIdToSetCategory(Set<Long> setId) {
        return setId.stream()
                .map(i -> categoryService.findById(i)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Категория не найдена, идентификатор: " + i)))
                .collect(Collectors.toSet());
    }
}
