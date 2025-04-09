package com.gag.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.OffsetDateTime;

@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    public Card(String title, String description, OffsetDateTime createDt, OffsetDateTime updateDt, Boolean isBlocked, BoardColumn boardColumn) {
        this.title = title;
        this.description = description;
        this.createDt = createDt;
        this.updateDt = updateDt;
        this.isBlocked = isBlocked;
        this.boardColumn = boardColumn;
    }
    public Card(){}
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
    private String blockedReason;

    @ToString.Include
    private String unblockedReason;

    @ToString.Include
    private OffsetDateTime lastBlockedDt;

    @ToString.Include
    private OffsetDateTime lastUnblockedDt;

    @ToString.Include
    private OffsetDateTime lastMovementDt;

    @ManyToOne
    @JoinColumn(name = "column_board_id")
    private BoardColumn boardColumn;

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public void setLastBlockedDt(OffsetDateTime lastBlockedDt) {
        this.lastBlockedDt = lastBlockedDt;
    }

    public void setLastUnblockedDt(OffsetDateTime lastUnblockedDt) {
        this.lastUnblockedDt = lastUnblockedDt;
    }

    public void setLastMovementDt(OffsetDateTime lastMovementDt) {
        this.lastMovementDt = lastMovementDt;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
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

    public BoardColumn getBoardColumn() {
        return boardColumn;
    }

    public void setBoardColumn(BoardColumn boardColumn) {
        this.boardColumn = boardColumn;
    }

    public OffsetDateTime getLastBlockedDt() {
        return lastBlockedDt;
    }

    public OffsetDateTime getLastUnblockedDt() {
        return lastUnblockedDt;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public OffsetDateTime getLastMovementDt() {
        return lastMovementDt;
    }
}
