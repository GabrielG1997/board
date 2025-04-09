package com.gag.board.repository;

import com.gag.board.entity.BoardColumn;
import com.gag.board.entity.audit.AuditBoardColumns;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findByBoard_Id(Long id);

    List<AuditBoardColumns> findAuditBoardColumnsByBoard_Id(Long id);

    AuditBoardColumns findByCardList_Id(Long id);
}