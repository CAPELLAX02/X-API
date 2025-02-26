package com.x.backend.exceptions;

public class ReportNotFoundException extends NotFoundException {
    public ReportNotFoundException(Long id) {
        super("Report with ID: " + id + " not found.");
    }
}
