package ru.nhp.core.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nhp.api.dto.core.BannerDto;
import ru.nhp.core.entities.Banner;

@Component
@RequiredArgsConstructor
public class BannerConverter {

    public Banner dtoToEntity(BannerDto bannerDto) {
        return Banner.builder()
                .id(bannerDto.getId())
                .title(bannerDto.getTitle())
                .imageId(bannerDto.getImageId())
                .build();
    }

    public BannerDto entityToDto(Banner banner) {
        return BannerDto.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .imageId(banner.getImageId())
                .build();
    }
}
