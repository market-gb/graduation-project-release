package ru.nhp.core.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.nhp.api.dto.core.BannerDto;
import ru.nhp.api.dto.core.CategoryDto;
import ru.nhp.api.exceptions.AppError;
import ru.nhp.api.exceptions.InvalidParamsException;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.core.converters.BannerConverter;
import ru.nhp.core.entities.Banner;
import ru.nhp.core.entities.Image;
import ru.nhp.core.services.BannerService;
import ru.nhp.core.services.StorageService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
@Tag(name = "Акции", description = "Методы работы с акциями")
public class BannerController {
    private final StorageService storageService;
    private final BannerService bannerService;
    private final BannerConverter bannerConverter;
    private final boolean[] freeBanner = new boolean[] {false, false, false, false, false};

    @GetMapping
    public List<BannerDto> fingAll() {
        return bannerService.findAll();
    }

    @Operation(
            summary = "Запрос на получение акции по идентификатору",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BannerDto.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public BannerDto getById(
            @PathVariable @Parameter(description = "Идентификатор акции", required = true) Long id) {
        Banner banner = bannerService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Акция не найдена, идентификатор: " + id));

        return bannerConverter.entityToDto(banner);
    }

    @Operation(
            summary = "Запрос на получение картинки акции по идентификатору",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = BannerDto.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "404",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @GetMapping(value = "/banner/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable @Parameter(description = "Идентификатор картинки акции", required = true) Long id) {
        List<BannerDto> bannerDtos = bannerService.findAll();
        for (BannerDto bannerDto : bannerDtos) {
            if (Long.parseLong(bannerDto.getTitle().substring(0,1)) == id) {
                Image imgFile = storageService.findById(bannerDto.getImageId()).orElse(null);
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(imgFile.getContent());
            }
        }
        return ResponseEntity
                .badRequest()
                .body(null);
    }

    @Operation(
            summary = "Создание новой акции",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = CategoryDto.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public BannerDto save(@RequestParam("title") String title,
                          @RequestParam("file") MultipartFile file) {

        //TODO Сделать проверку полей на валидность

        for (int i = 0; i < 5; i++) {
            if (freeBanner[i]) {
                Banner banner = bannerService.findByTitleContaining(i + 1 + "deleted").orElse(null);
                banner.setTitle(i + 1 + " " + title);
                storageService.delete(banner.getImageId());
                banner.setImageId(storageService.store(file, "banner", ".png"));
                freeBanner[i] = false;
                return bannerConverter.entityToDto(bannerService.tryToSave(banner));
            }
        }
        throw new InvalidParamsException("Все акции занаты");
    }

    @Operation(
            summary = "Изменение акции",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "201",
                            content = @Content(schema = @Schema(implementation = BannerDto.class))
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @PatchMapping
    public BannerDto update(@RequestBody @Parameter(description = "Изменённая акция", required = true) @Valid BannerDto bannerDto,
                              @Parameter(description = "Ошибки валидации", required = true) BindingResult bindingResult) {
        return bannerConverter.entityToDto(bannerService.tryToSave(bannerDto, bindingResult));
    }

    @Operation(
            summary = "Удаление акции",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Ошибка", responseCode = "400",
                            content = @Content(schema = @Schema(implementation = AppError.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    @Transactional
    public void deleteById(@PathVariable @Parameter(description = "Идентификатор акции", required = true) Long id) {
        Banner bannerById = bannerService.findById(id).orElse(null);
        freeBanner[Integer.parseInt(bannerById.getTitle().substring(0,1)) - 1] = true;
        storageService.delete((Objects.requireNonNull(bannerById)).getImageId(), "banner");
        bannerService.deleteById(id);
    }
}
