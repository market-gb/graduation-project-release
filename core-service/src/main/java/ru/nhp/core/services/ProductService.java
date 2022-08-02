package ru.nhp.core.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.api.exceptions.InvalidParamsException;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.api.exceptions.ValidationException;
import ru.nhp.core.converters.ProductConverter;
import ru.nhp.core.entities.Product;
import ru.nhp.core.repositories.ProductRepository;
import ru.nhp.core.repositories.specifications.ProductSpecifications;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productsRepository;
    private final ProductConverter productConverter;

    public Page<Product> findAll(Integer minPrice, Integer maxPrice, String partTitle, String categoryTitle, Long categoryId, Integer page, Integer pageSize) {
        Specification<Product> spec = Specification.where(null);
        if (minPrice != null) {
            spec = spec.and(ProductSpecifications.priceGreaterOrEqualsThan(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpecifications.priceLessThanOrEqualsThan(maxPrice));
        }
        if (partTitle != null) {
            spec = spec.and(ProductSpecifications.titleLike(partTitle));
        }
        if (categoryTitle != null) {
            spec = spec.and(ProductSpecifications.findByCategory(categoryTitle));
        }
        if (categoryId != null) {
            spec = spec.and(ProductSpecifications.findByCategoryId(categoryId));
        }
        return productsRepository.findAll(spec, PageRequest.of(page - 1, pageSize));
    }

    public Optional<Product> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return productsRepository.findById(id);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new InvalidParamsException("Невалидный параметр идентификатор:" + null);
        }
        try {
            productsRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Ошибка удаления товара. Товар " + id + "не существует");
        }
    }

    public Product tryToSave(ProductDto productDto, BindingResult bindingResult) {
        if (productDto == null) {
            throw new InvalidParamsException("Невалидный параметр 'productDto':" + null);
        }
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new ValidationException("Ошибка валидации", errors);
        }
        return save(productConverter.dtoToEntity(productDto));
    }

    private Product save(Product product) {
        if (product == null) {
            throw new InvalidParamsException("Невалидный параметр 'product':" + null);
        }
        if (product.getId() == null && isTitlePresent(product.getTitle())) {
            throw new InvalidParamsException("Товар с таким наименованием уже существует:" + product.getTitle());
        }
        return productsRepository.save(product);
    }

    private Boolean isTitlePresent(String title) {
        return productsRepository.countByTitle(title) > 0;
    }

    public Page<Product> findAllByCategoryId(Long id, Integer page) {
        return productsRepository.findAllByCategoryId(id, PageRequest.of(page - 1, 8));
    }
}
