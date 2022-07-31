package ru.nhp.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nhp.user.entites.Role;

@Repository
public interface AuthorityRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}