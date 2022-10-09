package ru.nhp.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import ru.nhp.api.dto.core.BannerDto;
import ru.nhp.api.exceptions.InvalidParamsException;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.api.exceptions.ValidationException;
import ru.nhp.core.converters.BannerConverter;
import ru.nhp.core.entities.Banner;
import ru.nhp.core.repositories.BannerRepository;
import ru.nhp.core.repositories.ImageDbRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;
    private final BannerConverter bannerConverter;
    private final StorageService storageService;
    private final ImageDbRepository imageDbRepository;

    @PostConstruct
    void init(){
        if (!imageDbRepository.existsById(104L)) {
            Long next = imageDbRepository.findMaxId().orElse(0L) + 1;
            Long first = imageDbRepository.findMaxId().orElse(0L);
            for (Long i = 1L; i < 6; i++) {
                Banner banner = bannerRepository.findById(next - first).orElse(null);
                banner.setImageId(storageService.store(next, i, "banner"));
                save(banner);
                next++;
            }
        }
    }

    public List<BannerDto> findAll() {
        return bannerRepository.findAll().stream()
                .map(bannerConverter::entityToDto).collect(Collectors.toList());
    }

    public Optional<Banner> findById(Long id) {
        return bannerRepository.findById(id);
    }

    public void deleteById(Long id) {
        if (id == null) {
            throw new InvalidParamsException("Невалидный параметр идентификатор:" + null);
        }
        try {
            Banner banner = bannerRepository.findById(id).orElse(null);
            String bannerNumber = banner.getTitle().substring(0,1);
            banner.setTitle(bannerNumber + "deleted");
            bannerRepository.save(banner);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Ошибка удаления банера. Банер " + id + "не существует");
        }
    }

    public Optional<Banner> findByTitleContaining(String subString) {
        return bannerRepository.findByTitleContaining(subString);
    }

    public Banner tryToSave(Banner banner) {
        return save(banner);
    }

    public Banner tryToSave(BannerDto bannerDto, BindingResult bindingResult) {
        if (bannerDto == null) {
            throw new InvalidParamsException("Невалидный параметр 'bannerDto':" + null);
        }
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new ValidationException("Ошибка валидации", errors);
        }
        return save(bannerConverter.dtoToEntity(bannerDto));
    }

    private Banner save(Banner banner) {
        if (banner == null) {
            throw new InvalidParamsException("Невалидный параметр 'Акция':" + null);
        }
        if (banner.getId() == null && isTitlePresent(banner.getTitle())) {
            throw new InvalidParamsException("Банер с таким наименованием уже существует:" + banner.getTitle());
        }
        return bannerRepository.save(banner);
    }

    private Boolean isTitlePresent(String title) {
        return bannerRepository.countByTitle(title) > 0;
    }
}
