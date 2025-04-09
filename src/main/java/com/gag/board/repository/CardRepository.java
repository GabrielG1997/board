package com.gag.board.repository;

import com.gag.board.entity.Card;
import com.gag.board.entity.audit.AuditBoardColumns;
import com.gag.board.entity.audit.AuditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByBoardColumn_Board_Id(Long id);

    List<AuditCard> findAuditCardByBoardColumn_Board_Id(Long id);

    AuditCard findAuditCardById(Long id);

}