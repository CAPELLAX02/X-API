package com.x.backend.repositories;

import com.x.backend.models.message.Message;
import com.x.backend.models.message.MessageRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageReadRepository extends JpaRepository<MessageRead, Long> {

    Optional<MessageRead> findByUserIdAndMessage(Long userId, Message message);

}
