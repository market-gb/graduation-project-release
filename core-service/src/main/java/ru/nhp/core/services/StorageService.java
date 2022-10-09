package ru.nhp.core.services;

import org.springframework.web.multipart.MultipartFile;
import ru.nhp.core.entities.Image;

import java.util.Optional;

public interface StorageService {

    Long store(MultipartFile file, String imageType, String extension);

    Long store(Long nextId, Long iter, String imageType);
    void delete(Long id, String imageType);
    void delete(Long id);

    Optional<Image> findByName(String name);
    Optional<Image> findById(Long id);

}
