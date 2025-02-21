package com.x.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "image_name", unique = true)
    private String imageName;

    @Column(name = "image_type")
    private String imageType;

    @Column(name = "image_path")
    @JsonIgnore
    private String imagePath;

    @Column(name = "image_url")
    private String imageUrl;

    public Image() {}

    public Image(String imageName, String imageType, String imagePath, String imageUrl) {
        this.imageName = imageName;
        this.imageType = imageType;
        this.imagePath = imagePath;
        this.imageUrl = imageUrl;
    }

    public Image(Integer imageId, String imageName, String imageType, String imagePath, String imageUrl) {
        this.imageId = imageId;
        this.imageName = imageName;
        this.imageType = imageType;
        this.imagePath = imagePath;
        this.imageUrl = imageUrl;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Image image)) return false;
        return Objects.equals(getImageId(), image.getImageId())
                && Objects.equals(getImageName(), image.getImageName())
                && Objects.equals(getImageType(), image.getImageType())
                && Objects.equals(getImagePath(), image.getImagePath())
                && Objects.equals(getImageUrl(), image.getImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getImageId(),
                getImageName(),
                getImageType(),
                getImagePath(),
                getImageUrl()
        );
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", imageName='" + imageName + '\'' +
                ", imageType='" + imageType + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

}
