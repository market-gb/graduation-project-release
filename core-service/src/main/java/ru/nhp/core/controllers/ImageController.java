package ru.nhp.core.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nhp.core.entities.Image;
import ru.nhp.core.services.StorageService;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Tag(name = "Картинки", description = "Методы работы с картинками")
public class ImageController {
    private final StorageService storageService;

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable @Parameter(description = "Идентификатор категории", required = true) Long id) {
        Image imgFile = storageService.findById(id).orElse(null);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imgFile.getContent());
    }
}
