package com.gag.board.service;

import com.gag.board.entity.Board;
import com.gag.board.entity.BoardColumn;
import com.gag.board.entity.Card;
import com.gag.board.entity.audit.AuditBoardColumns;
import com.gag.board.repository.BoardColumnRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Service responsible for managing board columns, including retrieval and generation of columns for a board.
 */
@Service
public class BoardColumnService {
    private final BoardColumnRepository boardColumnRepository;
    /**
     * Constructs a BoardColumnService with the specified repository.
     *
     * @param boardColumnRepository repository for board column persistence
     */
    public BoardColumnService(BoardColumnRepository boardColumnRepository) {
        this.boardColumnRepository = boardColumnRepository;
    }
    /**
     * Finds all board columns associated with a specific board ID.
     *
     * @param id the ID of the board
     * @return a list of board columns belonging to the board
     */
    public List<BoardColumn> findByBoard_Id(long id) {
        return boardColumnRepository.findByBoard_Id(id);
    }
    /**
     * Generates a list of board columns for a given board, based on the specified quantity.
     * The columns are labeled as Initial, Pending, Finished, and Cancelled depending on their position.
     *
     * @param columnsQty the number of columns to generate
     * @param board      the board to associate with the columns
     * @return a list of generated board columns
     */
    public List<BoardColumn> generateBoardColumns(int columnsQty, Board board){
        List<BoardColumn> boardColumnList = new ArrayList<>();
        for (int i = 0; i < columnsQty; i++) {
            BoardColumn boardColumn = new BoardColumn();
            String name;
            char type;
            int order;
            if(i == 0){
                name = "Initial";
                type = 'I';
                order = i;
            } else if (i == columnsQty-2) {
                name = "Finished";
                type = 'F';
                order = i;
            }else if(i == columnsQty-1){
                name = "Cancelled";
                type = 'C';
                order = i;
            }else{
                name = "Pending";
                type = 'P';
                order = i;
            }
            boardColumn.setBoard(board);
            boardColumn.setName(name);
            boardColumn.setType(type);
            boardColumn.setBoard_order(order);
            boardColumn.setCreateDt(OffsetDateTime.now());
            boardColumn.setUpdateDt(OffsetDateTime.now());
            boardColumnList.add(boardColumn);
        }
        return boardColumnList;
    }
}
