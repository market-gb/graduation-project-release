package ru.nhp.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.nhp.api.dto.core.CategoryDto;
import ru.nhp.api.exceptions.InvalidParamsException;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.api.exceptions.ValidationException;
import ru.nhp.core.converters.CategoryConverter;
import ru.nhp.core.entities.Category;
import ru.nhp.core.repositories.CategoryRepository;
import ru.nhp.core.repositories.ImageDbRepository;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;
    private final StorageService storageService;
    private final ImageDbRepository imageDbRepository;

    @PostConstruct
    void init(){
        if (!imageDbRepository.existsById(104L)) {
            Long next = imageDbRepository.findMaxId().orElse(0L) + 1;
            Long first = imageDbRepository.findMaxId().orElse(0L);
            for (Long i = 1L; i < 10; i++) {
                Category category = categoryRepository.findById(next - first).orElse(null);
                category.setImageId(storageService.store(next, i, "category"));
                save(category);
                next++;
            }
        }
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Set<Category> findAllByIdButch(Set<Long> idSet){
        return new HashSet<>(categoryRepository.findAllById(idSet));
    }

    public Page<Category> searchCategories(Integer page) {
        return categoryRepository.findAll(PageRequest.of(page - 1, 10000));

    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new InvalidParamsException("Невалидный параметр идентификатор:" + null);
        }
        try {
            categoryRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Ошибка удаления категории. Категория " + id + "не существует");
        }
    }

    public Category tryToSave(CategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new InvalidParamsException("Невалидный параметр 'categoryDto':" + null);
        }
        return save(categoryConverter.dtoToEntity(categoryDto));
    }

    public Category tryToSave(CategoryDto categoryDto, BindingResult bindingResult) {
        if (categoryDto == null) {
            throw new InvalidParamsException("Невалидный параметр 'categoryDto':" + null);
        }
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new ValidationException("Ошибка валидации", errors);
        }
        return save(categoryConverter.dtoToEntity(categoryDto));
    }

    private Category save(Category category) {
        if (category == null) {
            throw new InvalidParamsException("Невалидный параметр 'categorys':" + null);
        }
        if (category.getId() == null && isTitlePresent(category.getTitle())) {
            throw new InvalidParamsException("Категория с таким наименованием уже существует:" + category.getTitle());
        }
        return categoryRepository.save(category);
    }

    private Boolean isTitlePresent(String title) {
        return categoryRepository.countByTitle(title) > 0;
    }

}
