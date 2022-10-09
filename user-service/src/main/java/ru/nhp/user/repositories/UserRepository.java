package ru.nhp.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nhp.user.entites.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String login);

    Optional<User> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "insert into users_roles(role_id, user_id) values('?1', '?2')", nativeQuery = true)
    void changeRole(Long roleId, Long userId);

    @Modifying
    @Query("update User u set u.updatedAt = CURRENT_TIMESTAMP where u.id = ?1")
    void changeUpdateAt(Long userId);

    @Modifying
    @Transactional
    @Query(value = "delete from users_roles where users_roles.user_id = ?1", nativeQuery = true)
    void deleteRoles(Long userId);
}
