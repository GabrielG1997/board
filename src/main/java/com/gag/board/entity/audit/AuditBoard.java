package com.gag.board.entity.audit;

import com.gag.board.entity.Board;
import com.gag.board.entity.BoardColumn;
import com.gag.board.entity.Card;
import com.gag.board.repository.BoardRepository;
import com.gag.board.service.BoardColumnService;
import com.gag.board.service.BoardService;
import com.gag.board.service.CardService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name="a_boards")
public class AuditBoard {
    @Id
    @Column(name = "board_id")
    private Long id;

    @ToString.Include
    private String name;

    @ToString.Include
    private OffsetDateTime createDt;

    @ToString.Include
    private OffsetDateTime updateDt;

    @OneToMany(mappedBy = "board")
    private List<AuditBoardColumns> boardColumns;

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

    public List<AuditBoardColumns> getBoardColumns() {
        return boardColumns;
    }

    public void setBoardColumns(List<AuditBoardColumns> boardColumns) {
        this.boardColumns = boardColumns;
    }
    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
