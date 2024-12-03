package admin_user.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import admin_user.model.SupportChat.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
