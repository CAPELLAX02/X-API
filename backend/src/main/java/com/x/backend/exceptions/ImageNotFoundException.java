package com.x.backend.exceptions;

public class ImageNotFoundException extends NotFoundException {
    public ImageNotFoundException(Long id) {
        super("Image with ID: " + id + " not found.");
    }
}
