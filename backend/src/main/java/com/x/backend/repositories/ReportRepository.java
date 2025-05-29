package com.x.backend.repositories;

import com.x.backend.models.user.Report;
import com.x.backend.models.user.enums.ReportStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends BaseRepository<Report, Long> {

    List<Report> findAllByStatus(ReportStatus status);

}
