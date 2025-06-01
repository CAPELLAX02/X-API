package com.x.backend.repositories;

import com.x.backend.models.media.Image;
import com.x.backend.models.media.enums.ImageType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends BaseRepository<Image, Long> {

    Optional<Image> findByImageUrl(String imageUrl);

    List<Image> findByImageType(ImageType imageType);

    @Query("SELECT i FROM Image i WHERE i.imageUrl LIKE CONCAT('%', :userId, '%') ORDER BY i.uploadedAt DESC")
    List<Image> findUserImages(Long userId);
}
