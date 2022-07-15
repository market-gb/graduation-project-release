package user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import user.entites.RegistrationToken;
import user.entites.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, Long> {

    @Query("SELECT rt.user FROM RegistrationToken rt WHERE rt.expiredAt > :time AND rt.token = :token")
    Optional<User> findUserByTimeAndToken(@Param("time") LocalDateTime time, @Param("token") String token);

    @Query("SELECT rt.user FROM RegistrationToken rt WHERE rt.token = :token")
    Optional<User> findUserByToken(String token);

    RegistrationToken findRegistrationTokenByToken(String token);
    RegistrationToken findRegistrationTokenByUser_Id(Long id);

    void delete(RegistrationToken registrationToken);
}
