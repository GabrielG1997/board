package com.gag.board.repository.audit;

import com.gag.board.entity.audit.AuditBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditBoardRepository extends JpaRepository<AuditBoard, Long> {

}