package com.x.backend.exceptions.image;

import com.x.backend.exceptions.BaseNotFoundException;

public class ImageNotFoundException extends BaseNotFoundException {
    public ImageNotFoundException(Long id) {
        super("Image with ID: " + id + " not found.");
    }
}
