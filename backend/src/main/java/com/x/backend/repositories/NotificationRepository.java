package com.x.backend.repositories;

import com.x.backend.models.notification.Notification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends BaseRepository<Notification, Long> {

    List<Notification> findByRecipientIdAndIsReadFalseOrderByCreatedAtDesc(Long recipientId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipient.id = :userId AND n.isRead = false")
    long countUnreadNotifications(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM x_db.public.notifications WHERE recipient_id = :userId ORDER BY created_at DESC LIMIT 10", nativeQuery = true)
    List<Notification> findRecentNotifications(@Param("userId") Long userId);
}
