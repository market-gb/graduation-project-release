package ru.nhp.core.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nhp.api.dto.core.CategoryDto;
import ru.nhp.core.entities.Category;

@Component
@RequiredArgsConstructor
public class CategoryConverter {

    public Category dtoToEntity(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .title(categoryDto.getTitle())
                .description(categoryDto.getDescription())
                .imageId(categoryDto.getImageId())
                .build();
    }

    public CategoryDto entityToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .title(category.getTitle())
                .description(category.getDescription())
                .imageId(category.getImageId())
                .build();
    }
}
