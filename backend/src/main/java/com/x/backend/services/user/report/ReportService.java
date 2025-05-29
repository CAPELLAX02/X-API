package com.x.backend.services.user.report;

import com.x.backend.dto.report.request.CreateReportRequest;
import com.x.backend.utils.api.BaseApiResponse;

public interface ReportService {

    BaseApiResponse<String> reportUser(String reporterUsername, Long reportedUserId, CreateReportRequest req);

    BaseApiResponse<String> reportPost(String reporterUsername, Long reportedPostId, CreateReportRequest req);

    BaseApiResponse<String> reportComment(String reporterUsername, Long reportedCommentId, CreateReportRequest req);

}
