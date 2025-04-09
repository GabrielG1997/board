package com.gag.board.repository.audit;

import com.gag.board.entity.audit.AuditBoardColumns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditBoardColumnsRepository extends JpaRepository<AuditBoardColumns, Long> {
}