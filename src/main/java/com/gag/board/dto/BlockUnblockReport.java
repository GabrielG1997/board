package com.gag.board.dto;

import java.time.LocalDateTime;

public record BlockUnblockReport(
        Long id, String title, String description, String type, Long boardId, Long columnBoardId,
        boolean isBlocked, String blockedReason, String unblockedReason, LocalDateTime lastBlockedDt,
        LocalDateTime lastUnblockedDt, Long secondsSpent) {
}
