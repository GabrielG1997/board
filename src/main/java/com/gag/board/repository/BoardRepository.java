package com.gag.board.repository;

import com.gag.board.entity.Board;
import com.gag.board.entity.audit.AuditBoard;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    boolean existsByIdOrNameIgnoreCase(Long id, String name);
    @Override
    @NonNull
    Optional<Board> findById(@NonNull Long aLong);

    Optional<Board> findByIdOrNameIgnoreCase(Long id, String name);

    Board findBoardById(Long id);

    AuditBoard findAuditBoardById(Long id);
}