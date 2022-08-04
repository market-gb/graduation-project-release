package ru.nhp.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nhp.user.entites.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String login);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query(value = "update users_roles set role_id = ?1 where user_id = ?2", nativeQuery = true)
    void changeRole(Long roleId, Long userId);
}
