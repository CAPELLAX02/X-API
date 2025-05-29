package com.x.backend.models.user;

import com.x.backend.models.post.comment.Comment;
import com.x.backend.models.post.Post;
import com.x.backend.models.user.enums.ReportReasonType;
import com.x.backend.models.user.enums.ReportStatus;
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
    @Column(name = "report_id")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "reason_type", nullable = false)
    private ReportReasonType reasonType;

    @Column(name = "reason_description", length = 500)
    private String reasonDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportStatus status;

    @Column(name = "reported_at", nullable = false, updatable = false)
    private LocalDateTime reportedAt;

    @PrePersist
    public void prePersist() {
        this.reportedAt = LocalDateTime.now();
        this.status = ReportStatus.PENDING;
    }

    public Report() {}

    public Report(Long id,
                  ApplicationUser reportedBy,
                  ApplicationUser reportedUser,
                  Post reportedPost,
                  Comment reportedComment,
                  ReportReasonType reasonType,
                  String reasonDescription,
                  ReportStatus status,
                  LocalDateTime reportedAt
    ) {
        this.id = id;
        this.reportedBy = reportedBy;
        this.reportedUser = reportedUser;
        this.reportedPost = reportedPost;
        this.reportedComment = reportedComment;
        this.reasonType = reasonType;
        this.reasonDescription = reasonDescription;
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

    public ReportReasonType getReasonType() {
        return reasonType;
    }

    public void setReasonType(ReportReasonType reasonType) {
        this.reasonType = reasonType;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
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
