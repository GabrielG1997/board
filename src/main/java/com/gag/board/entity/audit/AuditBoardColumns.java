package com.gag.board.entity.audit;

import com.gag.board.entity.Board;
import com.gag.board.entity.BoardColumn;
import com.gag.board.entity.Card;
import com.gag.board.repository.BoardColumnRepository;
import com.gag.board.service.BoardColumnService;
import com.gag.board.service.BoardService;
import com.gag.board.service.CardService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "a_board_columns")
public class AuditBoardColumns {
    @ToString.Include
    @Id
    @Column(name = "column_board_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private AuditBoard board;

    @ToString.Include
    private String name;

    @ToString.Include
    private Integer board_order;

    @ToString.Include
    private char type;

    @ToString.Include
    private OffsetDateTime createDt;

    @ToString.Include
    private OffsetDateTime updateDt;


    @OneToMany(mappedBy = "columnBoard")
    private List<AuditCard> cardList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AuditBoard getBoard() {
        return board;
    }

    public void setBoard(AuditBoard board) {
        this.board = board;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBoard_order() {
        return board_order;
    }

    public void setBoard_order(Integer board_order) {
        this.board_order = board_order;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
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

    public List<AuditCard> getCardList() {
        return cardList;
    }

    public void setCardList(List<AuditCard> cardList) {
        this.cardList = cardList;
    }
}
