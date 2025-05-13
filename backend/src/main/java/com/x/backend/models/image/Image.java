package com.x.backend.models.image;

import com.x.backend.models.image.enums.ImageType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "images",
        indexes = {
                @Index(name = "idx_image_url", columnList = "image_url", unique = true),
                @Index(name = "idx_image_type", columnList = "image_type")
        }
)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "image_url", nullable = false, unique = true)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false, length = 20)
    private ImageType imageType;

    @Column(name = "size_in_kb", nullable = false)
    private Long sizeInKb;

    @Column(name = "width", nullable = false)
    private int width;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    public Image() {}

    public Image(
            Long id,
            String imageUrl,
            ImageType imageType,
            Long sizeInKb,
            int width,
            int height,
            LocalDateTime uploadedAt
    ) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.imageType = imageType;
        this.sizeInKb = sizeInKb;
        this.width = width;
        this.height = height;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public Long getSizeInKb() {
        return sizeInKb;
    }

    public void setSizeInKb(Long sizeInKb) {
        this.sizeInKb = sizeInKb;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

}
