package com.x.backend.controllers;

import com.x.backend.dto.report.request.CreateReportRequest;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.services.user.report.ReportService;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/user/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> reportUser(
            @AuthenticationPrincipal ApplicationUser reporterUser,
            @PathVariable("id") Long reportedUserId,
            @RequestBody @Valid CreateReportRequest req
    ) {
        BaseApiResponse<String> res = reportService.reportUser(reporterUser.getUsername(), reportedUserId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/post/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> reportPost(
            @AuthenticationPrincipal ApplicationUser reporterUser,
            @PathVariable("id") Long reportedPostId,
            @RequestBody @Valid CreateReportRequest req
    ) {
        BaseApiResponse<String> res = reportService.reportPost(reporterUser.getUsername(), reportedPostId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/comment/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> reportComment(
            @AuthenticationPrincipal ApplicationUser reporterUser,
            @PathVariable("id") Long reportedCommentId,
            @RequestBody @Valid CreateReportRequest req
    ) {
        BaseApiResponse<String> res = reportService.reportComment(reporterUser.getUsername(), reportedCommentId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
