package com.x.backend.dto.report.request;

import com.x.backend.models.user.enums.ReportReasonType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReportRequest(


        @NotNull(message = "Report reason is required.")
        ReportReasonType reportReasonType,

        @Size(max = 500, message = "Report reason description must be at most 500 characters.")
        String reportReasonDescription

) {
}
