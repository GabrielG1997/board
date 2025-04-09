package com.gag.board.service;

import com.gag.board.dto.BlockUnblockReport;
import com.gag.board.dto.MovementReport;
import com.gag.board.entity.Board;
import com.gag.board.entity.BoardColumn;
import com.gag.board.entity.Card;
import com.gag.board.repository.CardRepository;
import com.gag.board.repository.audit.AuditCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private AuditCardRepository auditCardRepository;
    @InjectMocks
    private CardService cardService;
    @Test
    void save_shouldCardRepositorySave(){
        Card card = new Card();
        cardService.save(card);
        verify(cardRepository).save(card);
    }
    @Test
    void shouldFindCardsByBoardIdWhenIdExists(){
        //given
        BoardColumn boardColumn = new BoardColumn();
        Card card = new Card("title","description", OffsetDateTime.now().minusHours(1), OffsetDateTime.now(),false, boardColumn);
        Mockito.when(cardRepository.findByBoardColumn_Board_Id(1L)).thenReturn(List.of(card));
        //when
        List<Card> cardList = cardService.getCards(1L);
        //then
        assertNotNull(cardList);
        assertEquals(1, cardList.size());
        assertEquals("title", cardList.getFirst().getTitle());
    }
    @Test
    void shouldCreateCardsWithMapOfTitleDescriptionForSpecifColumn(){
        //given
        Map<String,String> titlesAndDescriptions = new HashMap<>();
        titlesAndDescriptions.put("title_1","desc_1");
        titlesAndDescriptions.put("title_2","desc_2");
        BoardColumn boardColumn = new BoardColumn();
        //when
        List<Card> cardList = cardService.generateCards(titlesAndDescriptions,boardColumn);
        //then
        assertNotNull(cardList);
        assertEquals(2,cardList.size());
        for (Card card : cardList) {
            assertTrue(titlesAndDescriptions.containsKey(card.getTitle()));
            assertEquals(titlesAndDescriptions.get(card.getTitle()), card.getDescription());
        }
    }
    @Test
    void shouldGetMovementReportWhenBoardIdExists(){
        //given
        Board board = new Board();
        board.setId(1L);
        Object[] row1 = new Object[]{1L, "title_1", "desc_1", "type_1", 1L, 1L, OffsetDateTime.now().minusDays(1), 20.0};
        Object[] row2 = new Object[]{2L, "title_2", "desc_2", "type_2", 1L, 1L, OffsetDateTime.now().minusDays(1), 10.0};
        List<Object[]> mockResult = List.of(row1, row2);
        Mockito.when(auditCardRepository.getCardMovementReport(board.getId())).thenReturn(mockResult);
        // when
        List<Object[]> movementReportArray = auditCardRepository.getCardMovementReport(board.getId());
        List<MovementReport> movementReportList = movementReportArray.stream().map(row -> new MovementReport(
                (Long) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3],
                (Long) row[4],
                (Long) row[5],
                row[6] != null ? ((OffsetDateTime) row[6]).toLocalDateTime() : null,
                (Double) row[7]
        )).toList();
        //then
        assertNotNull(movementReportList);
        assertEquals(2,movementReportList.size());
        for (MovementReport report : movementReportList){
            assertNotNull(report.exitTime());
            assertNotNull(report.type());
            assertTrue(report.minutesSpent() > 0);
        }
    }
    @Test
    void getBlockedReportWhenBoardIdExists(){
        //given
        Board board = new Board();
        board.setId(1L);
        Object[] row1 = new Object[]{1L, "title_1", "desc_1", "type_1", 1L, 1L,true,"blockReason","unblockReason", OffsetDateTime.now(), OffsetDateTime.now(), 20L};
        Object[] row2 = new Object[]{2L, "title_2", "desc_2", "type_2", 1L, 1L,true,"blockReason","unblockReason", OffsetDateTime.now(), OffsetDateTime.now(), 20L};
        List<Object[]> mockResult = List.of(row1, row2);
        Mockito.when(auditCardRepository.getCardBlockedReport(board.getId())).thenReturn(mockResult);
        // when
        List<Object[]> blockUnblockReportReportArray = auditCardRepository.getCardBlockedReport(board.getId());
        List<BlockUnblockReport> blockUnblockReportList = blockUnblockReportReportArray.stream().map(row -> new BlockUnblockReport(
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
        //then
        assertNotNull(blockUnblockReportList);
        assertEquals(2,blockUnblockReportList.size());
        for (BlockUnblockReport report : blockUnblockReportList){
            assertNotNull(report.id());
            assertNotNull(report.type());
            assertTrue(report.secondsSpent() > 0);
        }
    }
    @Test
    void getCardsWhenBoardIdExists(){
        //given
        Board board = new Board();
        board.setId(1L);
        BoardColumn boardColumn = new BoardColumn();
        List<Card> cardList = new ArrayList<>(2);
        cardList.add(0, new Card("title", "desc", OffsetDateTime.now(), OffsetDateTime.now(), true, boardColumn));
        cardList.add(1, new Card("title_2", "desc_2", OffsetDateTime.now(), OffsetDateTime.now(), true, boardColumn));
        //when
        Mockito.when(cardRepository.findByBoardColumn_Board_Id(board.getId())).thenReturn(cardList);
        List<Card> cardListMock = cardService.getCards(board.getId());
        //then
        assertNotNull(cardListMock);
        for (Card card : cardListMock){
            assertNotNull(card.getTitle());
            assertNotNull(card.getDescription());
            assertTrue(card.getIsBlocked());
        }
    }
}
