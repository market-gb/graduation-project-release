package ru.nhp.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nhp.core.entities.Image;

import java.util.Optional;

@Repository
public interface ImageDbRepository extends JpaRepository <Image, Long> {
    void deleteImageById(Long imageId);

    @Query("select max(i.id) from Image i")
    Optional<Long> findMaxId();

    Optional<Image> findByName(String name);
}
