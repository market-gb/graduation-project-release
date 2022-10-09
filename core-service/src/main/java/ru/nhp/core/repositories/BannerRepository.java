package ru.nhp.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nhp.core.entities.Banner;

import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query("select count(b) from Banner b where b.title = ?1")
    Long countByTitle(String title);

    Optional<Banner> findByTitleContaining(String subString);

}

