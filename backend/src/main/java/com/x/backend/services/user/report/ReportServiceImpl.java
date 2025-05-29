package com.x.backend.services.user.report;

import com.x.backend.dto.report.request.CreateReportRequest;
import com.x.backend.exceptions.post.CommentNotFoundException;
import com.x.backend.exceptions.post.PostNotFoundException;
import com.x.backend.exceptions.user.UserNotFoundByIdException;
import com.x.backend.models.post.Post;
import com.x.backend.models.post.comment.Comment;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.Report;
import com.x.backend.models.user.enums.ReportStatus;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.repositories.CommentRepository;
import com.x.backend.repositories.PostRepository;
import com.x.backend.repositories.ReportRepository;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ApplicationUserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

    public ReportServiceImpl(ApplicationUserRepository userRepository,
                             PostRepository postRepository,
                             CommentRepository commentRepository,
                             ReportRepository reportRepository
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.reportRepository = reportRepository;
    }

    @Override
    public BaseApiResponse<String> reportUser(String reporterUsername, Long reportedUserId, CreateReportRequest req) {
        ApplicationUser reporterUser = userRepository.findByUsername(reporterUsername)
                .orElseThrow(() -> new UsernameNotFoundException(reporterUsername));

        ApplicationUser reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new UserNotFoundByIdException(reportedUserId));

        Report report = new Report();
        report.setReportedBy(reporterUser);
        report.setReportedUser(reportedUser);
        report.setReasonType(req.reportReasonType());
        report.setReasonDescription(req.reportReasonDescription());
        report.setStatus(ReportStatus.PENDING);

        reportRepository.save(report);

        return BaseApiResponse.success("User has been reported.");
    }

    @Override
    public BaseApiResponse<String> reportPost(String reporterUsername, Long reportedPostId, CreateReportRequest req) {
        ApplicationUser reporterUser = userRepository.findByUsername(reporterUsername)
                .orElseThrow(() -> new UsernameNotFoundException(reporterUsername));

        Post postToBeReported = postRepository.findById(reportedPostId)
                .orElseThrow(() -> new PostNotFoundException(reportedPostId));

        Report report = new Report();
        report.setReportedBy(reporterUser);
        report.setReportedPost(postToBeReported);
        report.setReportedUser(postToBeReported.getAuthor());
        report.setReasonType(req.reportReasonType());
        report.setReasonDescription(req.reportReasonDescription());
        report.setStatus(ReportStatus.PENDING);

        reportRepository.save(report);

        return BaseApiResponse.success("Post has been reported.");
    }

    @Override
    public BaseApiResponse<String> reportComment(String reporterUsername, Long reportedCommentId, CreateReportRequest req) {
        ApplicationUser reporterUser = userRepository.findByUsername(reporterUsername)
                .orElseThrow(() -> new UsernameNotFoundException(reporterUsername));

        Comment commentToBeReported = commentRepository.findById(reportedCommentId)
                .orElseThrow(() -> new CommentNotFoundException(reportedCommentId));

        Report report = new Report();
        report.setReportedBy(reporterUser);
        report.setReportedComment(commentToBeReported);
        report.setReportedUser(commentToBeReported.getAuthor());
        report.setReasonType(req.reportReasonType());
        report.setReasonDescription(req.reportReasonDescription());
        report.setStatus(ReportStatus.PENDING);

        reportRepository.save(report);

        return BaseApiResponse.success("Comment has been reported.");
    }

}
