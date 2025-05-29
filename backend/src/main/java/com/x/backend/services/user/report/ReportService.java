package com.x.backend.services.user.report;

import com.x.backend.dto.report.request.CreateReportRequest;
import com.x.backend.dto.report.response.ReportResponse;
import com.x.backend.models.user.enums.ReportStatus;

import java.util.List;

public interface ReportService {

    void createReport(String reporterUsername, CreateReportRequest req);

    List<ReportResponse> getReports(String reporterUsername);

    void updateReportStatus(Long reportId, ReportStatus newStatus);

}
