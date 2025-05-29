package com.x.backend.dto.report.response;

import com.x.backend.models.user.enums.ReportReasonType;
import com.x.backend.models.user.enums.ReportStatus;

import java.time.LocalDateTime;

public record ReportResponse(

        Long id,
        String reportedBy,
        String reportedUser,
        Long reportedPostId,
        Long reportedCommentId,
        ReportReasonType reportReasonType,
        String reasonDescription,
        ReportStatus reportStatus,
        LocalDateTime reportedAt

) {
}
