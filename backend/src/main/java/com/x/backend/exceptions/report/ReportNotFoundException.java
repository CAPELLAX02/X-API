package com.x.backend.exceptions.report;

import com.x.backend.exceptions.NotFoundException;

public class ReportNotFoundException extends NotFoundException {
    public ReportNotFoundException(Long id) {
        super("Report with ID: " + id + " not found.");
    }
}
