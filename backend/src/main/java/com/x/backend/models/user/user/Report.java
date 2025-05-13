package com.x.backend.models.user.user;

import com.x.backend.models.post.comment.Comment;
import com.x.backend.models.post.Post;
import com.x.backend.models.user.user.enums.ReportStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "reports",
        indexes = {
                @Index(name = "idx_report_reported_user", columnList = "reported_user_id"),
                @Index(name = "idx_report_post", columnList = "reported_post_id"),
                @Index(name = "idx_report_comment", columnList = "reported_comment_id")
        }
)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by", nullable = false)
    private ApplicationUser reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id")
    private ApplicationUser reportedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_post_id")
    private Post reportedPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_comment_id")
    private Comment reportedComment;

    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReportStatus status;

    @Column(name = "reported_at", nullable = false, updatable = false)
    private LocalDateTime reportedAt;

    public Report() {}

    public Report(
            Long id,
            ApplicationUser reportedBy,
            ApplicationUser reportedUser,
            Post reportedPost,
            Comment reportedComment,
            String reason,
            ReportStatus status,
            LocalDateTime reportedAt
    ) {
        this.id = id;
        this.reportedBy = reportedBy;
        this.reportedUser = reportedUser;
        this.reportedPost = reportedPost;
        this.reportedComment = reportedComment;
        this.reason = reason;
        this.status = status;
        this.reportedAt = reportedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationUser getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(ApplicationUser reportedBy) {
        this.reportedBy = reportedBy;
    }

    public ApplicationUser getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(ApplicationUser reportedUser) {
        this.reportedUser = reportedUser;
    }

    public Post getReportedPost() {
        return reportedPost;
    }

    public void setReportedPost(Post reportedPost) {
        this.reportedPost = reportedPost;
    }

    public Comment getReportedComment() {
        return reportedComment;
    }

    public void setReportedComment(Comment reportedComment) {
        this.reportedComment = reportedComment;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public LocalDateTime getReportedAt() {
        return reportedAt;
    }

    public void setReportedAt(LocalDateTime reportedAt) {
        this.reportedAt = reportedAt;
    }

}
