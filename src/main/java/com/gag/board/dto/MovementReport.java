package com.gag.board.dto;

import java.time.LocalDateTime;
public record MovementReport(
        Long id,
        String title,
        String description,
        String type,
        Long boardId,
        Long columnBoardId,
        LocalDateTime exitTime,
        Double minutesSpent) {
}
