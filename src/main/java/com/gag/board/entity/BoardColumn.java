package com.gag.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;

@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "board_columns")
public class BoardColumn {
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "column_board_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

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

    @OneToMany(mappedBy = "boardColumn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cardList;

    public Board getBoard() {
        return board;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setBoard(Board board) {
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

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }
}
