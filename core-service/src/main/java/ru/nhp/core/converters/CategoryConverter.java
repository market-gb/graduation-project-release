package ru.nhp.core.converters;

import org.springframework.stereotype.Component;
import ru.nhp.api.dto.core.CategoryDto;
import ru.nhp.core.entities.Category;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CategoryConverter {
    public Set<Category> setDtoToSetEntities(Set<CategoryDto> categoryDtoSet) {
        return categoryDtoSet.stream().map(this::dtoToEntity).collect(Collectors.toSet());
    }

    public Set<CategoryDto> setEntitiesToSetDto(Set<Category> categories) {
        return categories.stream().map(this::entityToDto).collect(Collectors.toSet());
    }

    private Category dtoToEntity(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getTitle());
    }

    private CategoryDto entityToDto(Category category) {
        return new CategoryDto(category.getId(), category.getTitle());
    }
}
