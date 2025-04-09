package com.gag.board.repository.audit;

import com.gag.board.dto.BlockUnblockReport;
import com.gag.board.dto.MovementReport;
import com.gag.board.entity.audit.AuditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuditCardRepository extends JpaRepository<AuditCard, Long> {

    @Query("""
        SELECT
            cm.id, cm.title, cm.description, CAST(bc.type AS string), bc.board.id, cm.columnBoard.id,
            COALESCE(
                LEAD(cm.lastMovementDt) OVER (PARTITION BY cm.id ORDER BY cm.lastMovementDt),
                NULL) AS exitTime,
            DATEDIFF(
                SECOND, COALESCE(cm.lastMovementDt, cm.createDt),
                COALESCE( LEAD(cm.lastMovementDt) OVER (PARTITION BY cm.id ORDER BY cm.lastMovementDt), GETDATE() ) ) / 60 AS minutesSpent
        
        FROM AuditCard cm
        LEFT JOIN AuditBoardColumns bc ON cm.columnBoard.id = bc.id AND bc.board.id = :boardId
        WHERE
            cm.lastMovementDt IS NOT NULL
            OR cm.createDt IS NOT NULL
        ORDER BY cm.id, cm.updateDt
    """)
    List<Object[]> getCardMovementReport(@Param("boardId")long boardId);

    @Query("""
        SELECT
            cm.id,
            cm.title,
            cm.description,
            CAST(bc.type AS string),
            bc.board.id,
            cm.columnBoard.id,
            cm.isBlocked,
            cm.blockedReason,
            cm.unblockedReason,
            cm.lastBlockedDt,
            cm.lastUnblockedDt,
            CASE
                WHEN cm.lastUnblockedDt IS NULL
                THEN
                null
                ELSE
                DATEDIFF(
                SECOND, cm.lastBlockedDt,
                COALESCE(cm.lastUnblockedDt, GETDATE())
            ) END AS secondsSpent
        FROM AuditCard cm
        LEFT JOIN AuditBoardColumns bc
            ON cm.columnBoard.id = bc.id AND bc.board.id = :boardId
        WHERE cm.lastBlockedDt IS NOT NULL AND cm.lastUnblockedDt IS NOT NULL
        ORDER BY cm.id, cm.lastBlockedDt
    """)
    List<Object[]> getCardBlockedReport(@Param("boardId")long boardId);
}