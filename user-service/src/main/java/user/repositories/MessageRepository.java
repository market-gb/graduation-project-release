package user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user.entites.MessageTextElement;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageTextElement, Long> {
    List<MessageTextElement> getByTitleContaining(String title);
}
