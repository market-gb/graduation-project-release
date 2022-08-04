package ru.nhp.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nhp.user.entites.Role;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    @Query("select r.name from Role r")
    List<String> findAllNames();
}
