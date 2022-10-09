package ru.nhp.core.services;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.nhp.core.entities.Image;
import ru.nhp.core.repositories.ImageDbRepository;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileSystemStorageService implements StorageService {
    private final ImageDbRepository imageDbRepository;

    @Override
    @Transactional
    public Long store(MultipartFile file, String imageType, String extension) {
        try {
            Image dbImage = new Image();
            dbImage.setName(imageType + extension);
            dbImage.setContent(file.getBytes());
            imageDbRepository.save(dbImage);

            Long imageId = imageDbRepository.findMaxId().orElse(null);
            dbImage = imageDbRepository.findById(imageId).orElse(null);
            dbImage.setName(imageType + imageId + extension);
            imageDbRepository.save(dbImage);

            return imageId;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения файла");
        }
    }

    @Override
    @Transactional
    public Long store(Long next, Long iter, String imageType) {
        try {
            Image dbImage = new Image();
            String extension;
            if (imageType.equals("banner")) extension = ".png";
            else extension = ".jpg";
            dbImage.setName(imageType + next + extension);
            ClassPathResource resource = new ClassPathResource("public/images/" + imageType + "s/" + imageType + iter + extension);
            byte[] content = FileCopyUtils.copyToByteArray(resource.getInputStream());
            dbImage.setContent(content);
            return imageDbRepository.save(dbImage).getId();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения файла");
        }
    }

    @Override
    public void delete(Long imageId) {
        imageDbRepository.deleteImageById(imageId);
    }

    @Override
    public void delete(Long imageId, String imageType) {
        try {
            String extension;
            if (imageType.equals("banner")) extension = ".png";
            else extension = ".jpg";
            Image dbImage = imageDbRepository.findById(imageId).orElse(null);
            dbImage.setName("deleted" + imageId + extension);
            ClassPathResource resource = new ClassPathResource("public/images/" + imageType + "s/" + imageType + 1 + extension);
            byte[] content = FileCopyUtils.copyToByteArray(resource.getInputStream());
            dbImage.setContent(content);
            imageDbRepository.save(dbImage);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения файла");
        }
    }

    @Override
    public Optional<Image> findByName(String name) {
        return imageDbRepository.findByName(name);
    }

    @Override
    public Optional<Image> findById(Long id) {
        return imageDbRepository.findById(id);
    }
}
