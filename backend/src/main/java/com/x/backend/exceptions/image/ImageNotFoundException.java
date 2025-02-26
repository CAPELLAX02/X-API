package com.x.backend.exceptions.image;

import com.x.backend.exceptions.NotFoundException;

public class ImageNotFoundException extends NotFoundException {
    public ImageNotFoundException(Long id) {
        super("Image with ID: " + id + " not found.");
    }
}
