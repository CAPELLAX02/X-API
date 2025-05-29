package com.x.backend.utils.builder;

import com.x.backend.dto.report.response.ReportResponse;
import com.x.backend.models.user.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportResponseBuilder {

    public ReportResponse build(Report r) {
        return new ReportResponse(
                r.getId(),
                r.getReportedBy().getUsername(),
                r.getReportedUser() != null ? r.getReportedUser().getUsername() : null,
                r.getReportedPost() != null ? r.getReportedPost().getId() : null,
                r.getReportedComment() != null ? r.getReportedComment().getId() : null,
                r.getReasonType(),
                r.getReasonDescription(),
                r.getStatus(),
                r.getReportedAt()
        );
    }

}
