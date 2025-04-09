package com.gag.board.entity.audit;

import com.gag.board.entity.Board;
import com.gag.board.entity.BoardColumn;
import com.gag.board.entity.Card;
import com.gag.board.service.BoardColumnService;
import com.gag.board.service.CardService;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "a_cards")

public class AuditCard {
    @ToString.Include
    @Id
    @Column(name = "card_id")
    private Long id;

    @ToString.Include
    private String title;

    @ToString.Include
    private String description;

    @ToString.Include
    private OffsetDateTime createDt;

    @ToString.Include
    private OffsetDateTime updateDt;

    @ToString.Include
    private Boolean isBlocked;
    @ToString.Include
    private OffsetDateTime lastBlockedDt;

    @ToString.Include
    private OffsetDateTime lastUnblockedDt;

    @ToString.Include
    private OffsetDateTime lastMovementDt;

    @ToString.Include
    private String blockedReason;

    @ToString.Include
    private String unblockedReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OffsetDateTime getCreateDt() {
        return createDt;
    }

    public void setCreateDt(OffsetDateTime createDt) {
        this.createDt = createDt;
    }

    public OffsetDateTime getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(OffsetDateTime updateDt) {
        this.updateDt = updateDt;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public String getBlockedReason() {
        return blockedReason;
    }

    public void setBlockedReason(String blockedReason) {
        this.blockedReason = blockedReason;
    }

    public String getUnblockedReason() {
        return unblockedReason;
    }

    public void setUnblockedReason(String unblockedReason) {
        this.unblockedReason = unblockedReason;
    }

    public AuditBoardColumns getColumnBoard() {
        return columnBoard;
    }

    public void setColumnBoard(AuditBoardColumns columnBoard) {
        this.columnBoard = columnBoard;
    }

    @ManyToOne
    @JoinColumn(name = "column_board_id")
    private AuditBoardColumns columnBoard;

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
