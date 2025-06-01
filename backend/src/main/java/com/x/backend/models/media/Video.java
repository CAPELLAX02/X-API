package com.x.backend.models.media;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "video_url", nullable = false, unique = true)
    private String videoUrl;

    @Column(name = "size_in_kb", nullable = false)
    private Long sizeInKb;

    @Column(name = "duration_in_seconds", nullable = false)
    private Long durationInSeconds;

    @Column(name = "width", nullable = false)
    private int width;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    public Video() {}

    public Video(Long id,
                 String videoUrl,
                 Long sizeInKb,
                 Long durationInSeconds,
                 int width,
                 int height,
                 LocalDateTime uploadedAt
    ) {
        this.id = id;
        this.videoUrl = videoUrl;
        this.sizeInKb = sizeInKb;
        this.durationInSeconds = durationInSeconds;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Long getSizeInKb() {
        return sizeInKb;
    }

    public void setSizeInKb(Long sizeInKb) {
        this.sizeInKb = sizeInKb;
    }

    public Long getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(Long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
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
