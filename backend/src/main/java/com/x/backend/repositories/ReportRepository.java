package com.x.backend.repositories;

import com.x.backend.models.user.user.Report;
import com.x.backend.models.user.user.enums.ReportStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends BaseRepository<Report, Long> {

    List<Report> findByReportedByIdOrderByReportedAtDesc(Long userId);

    List<Report> findByReportedPostIdOrderByReportedAtDesc(Long postId);

    List<Report> findByStatusOrderByReportedAtDesc(ReportStatus status);

    @Query("SELECT COUNT(r) FROM Report r WHERE r.reportedUser.id = :userId")
    long countUserReports(Long userId);
}
