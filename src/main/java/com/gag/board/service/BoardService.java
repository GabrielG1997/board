package com.gag.board.service;

import com.gag.board.dto.BlockUnblockReport;
import com.gag.board.dto.MovementReport;
import com.gag.board.entity.Board;
import com.gag.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Service responsible for managing board operations such as creation, deletion,
 * validation, and generating reports based on card data.
 */
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final CardService cardService;
    /**
     * Constructs a BoardService with required dependencies.
     *
     * @param boardRepository      repository for board persistence
     * @param cardService          service for managing cards and reports
     */
    public BoardService(BoardRepository boardRepository, CardService cardService) {
        this.boardRepository = boardRepository;
        this.cardService = cardService;
    }
    /**
     * Saves the given board to the database.
     *
     * @param board the board to be saved
     */
    @Transactional
    public void save(Board board){
        boardRepository.save(board);
    }
    /**
     * Creates a new Board object with the specified name and current creation date.
     *
     * @param boardName the name of the board
     * @return a new Board instance
     */
    public Board addBoard(String boardName){
        Board board = new Board();
        board.setName(boardName);
        board.setCreateDt(OffsetDateTime.now());
        return board;
    }
    /**
     * Deletes the specified board from the database.
     *
     * @param board the board to be deleted
     */
    public void delete(Board board){
        boardRepository.delete(board);
    }
    /**
     * Checks if a board exists by its ID or name (case-insensitive).
     *
     * @param id   the ID of the board
     * @param name the name of the board
     * @return true if the board exists, false otherwise
     */
    public boolean existsByIdOrNameIgnoreCase(Long id, String name){
        return boardRepository.existsByIdOrNameIgnoreCase(id,name);
    }
    /**
     * Finds a board by its ID or name (case-insensitive).
     *
     * @param id   the ID of the board
     * @param name the name of the board
     * @return an Optional containing the board if found, or empty otherwise
     */
    public Optional<Board> findByIdOrNameIgnoreCase(Long id, String name){
        return boardRepository.findByIdOrNameIgnoreCase(id,name);
    }
    /**
     * Retrieves a report of card movements for the specified board.
     *
     * @param boardId the ID of the board
     * @return a list of movement report entries
     */
    public List<MovementReport> getMovementReport(long boardId){
        return cardService.getMovementReport(boardId);
    }
    /**
     * Retrieves a report of blocked/unblocked cards for the specified board.
     *
     * @param boardId the ID of the board
     * @return a list of block/unblock report entries
     */
    public List<BlockUnblockReport> getBlockedReport(long boardId){

        return cardService.getBlockedReport(boardId);
    }
}
