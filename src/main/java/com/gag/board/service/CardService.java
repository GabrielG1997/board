package com.gag.board.service;

import com.gag.board.dto.BlockUnblockReport;
import com.gag.board.dto.MovementReport;
import com.gag.board.entity.BoardColumn;
import com.gag.board.entity.Card;
import com.gag.board.repository.CardRepository;
import com.gag.board.repository.audit.AuditCardRepository;
import org.springframework.cglib.core.Block;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
/**
 * Service responsible for managing cards and generating related reports.
 */
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final AuditCardRepository auditCardRepository;
    /**
     * Constructs a CardService with the provided repositories.
     *
     * @param cardRepository        repository for managing card persistence
     * @param auditCardRepository   repository for retrieving card audit data
     */
    public CardService(CardRepository cardRepository, AuditCardRepository auditCardRepository) {
        this.cardRepository = cardRepository;
        this.auditCardRepository = auditCardRepository;
    }
    /**
     * Saves a given card to the database.
     *
     * @param card the card to be saved
     */
    public void save(Card card){
        cardRepository.save(card);
    }
    /**
     * Retrieves all cards associated with a specific board.
     *
     * @param id the board ID
     * @return a list of cards linked to the board
     */
    public List<Card> findByBoardColumn_Board_Id(long id){
        return cardRepository.findByBoardColumn_Board_Id(id);
    }
    /**
     * Generates a list of new cards from given titles and descriptions,
     * assigning them to the specified board column.
     *
     * @param titlesAndDescriptions a map where the key is the title and the value is the description
     * @param boardColumn the column to which the cards will be assigned
     * @return a list of generated cards
     */
    public List<Card> generateCards(Map<String,String> titlesAndDescriptions, BoardColumn boardColumn) {

        List<Card> cardList = new ArrayList<>();
        for (int i = 0; i < titlesAndDescriptions.size(); i++) {
            Card card = new Card();
            card.setDescription(titlesAndDescriptions.values().stream().toList().get(i));
            card.setBoardColumn(boardColumn);
            card.setIsBlocked(false);
            card.setCreateDt(OffsetDateTime.now());
            card.setUpdateDt(null);
            card.setTitle(titlesAndDescriptions.keySet().stream().toList().get(i));
            cardList.add(card);
    }
        return cardList;
    }
    /**
     * Retrieves the movement report for cards in a given board.
     *
     * @param boardId the ID of the board
     * @return a list of movement report entries
     */
    public List<MovementReport> getMovementReport(long boardId){
        List<Object[]> results = auditCardRepository.getCardMovementReport(boardId);
        return results.stream().map(row -> new MovementReport(
                (Long) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3],
                (Long) row[4],
                (Long) row[5],
                row[6] != null ? ((OffsetDateTime) row[6]).toLocalDateTime() : null,
                (Double) row[7]
        )).toList();
    }
    /**
     * Retrieves the block/unblock report for cards in a given board.
     *
     * @param boardId the ID of the board
     * @return a list of block/unblock report entries
     */
    public List<BlockUnblockReport> getBlockedReport(long boardId){
        List<Object[]> results = auditCardRepository.getCardBlockedReport(boardId);
        return results.stream().map(row -> new BlockUnblockReport(
                (Long) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3],
                (Long) row[4],
                (Long) row[5],
                (Boolean) row[6],
                (String) row[7],
                (String) row[8],
                row[9] != null ? ((OffsetDateTime) row[9]).toLocalDateTime() : null,  // cm.lastBlockedDt
                row[10] != null ? ((OffsetDateTime) row[10]).toLocalDateTime() : null, // cm.lastUnblockedDt
                row[11] != null ? ((Number) row[11]).longValue() : null  // secondsSpent (evita erro se for Double)
        )).toList();
    }
    /**
     * Retrieves all cards associated with a given board ID.
     *
     * @param boardId the ID of the board
     * @return a list of cards
     */
    public List<Card> getCards(long boardId){
        return cardRepository.findByBoardColumn_Board_Id(boardId);
    }
}
