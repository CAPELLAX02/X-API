package com.x.backend.exceptions.image;

import com.x.backend.exceptions.BaseNotFoundException;

public class ImageBaseNotFoundException extends BaseNotFoundException {
    public ImageBaseNotFoundException(Long id) {
        super("Image with ID: " + id + " not found.");
    }
}
