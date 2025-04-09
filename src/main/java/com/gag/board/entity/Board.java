package com.gag.board.entity;

import com.gag.board.entity.audit.AuditBoardColumns;
import com.gag.board.entity.audit.AuditCard;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.OffsetDateTime;
import java.util.List;

@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name="boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    @ToString.Include
    private String name;

    @ToString.Include
    private OffsetDateTime createDt;

    @ToString.Include
    private OffsetDateTime updateDt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardColumn> boardColumns;

    public Board(Board board, List<AuditBoardColumns> auditBoardColumns, List<AuditCard> auditCards) {
    }
    public Board() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<BoardColumn> getBoardColumns() {
        return boardColumns;
    }

    public void setBoardColumns(List<BoardColumn> boardColumns) {
        this.boardColumns = boardColumns;
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }


}

