package admin_user.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import admin_user.model.SupportChat;

@Repository
public interface SupportChatRepository extends JpaRepository<SupportChat, Long> {
    List<SupportChat> findByUserId(Long userId);
}