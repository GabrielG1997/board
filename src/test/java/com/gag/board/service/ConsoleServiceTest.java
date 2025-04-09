package com.gag.board.service;

import com.gag.board.dto.BlockUnblockReport;
import com.gag.board.dto.MovementReport;
import com.gag.board.entity.Board;
import com.gag.board.entity.BoardColumn;
import com.gag.board.entity.Card;
import com.gag.board.util.exporter.ExcelExporter;
import com.gag.board.util.exporter.PDFExporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ConsoleServiceTest {

    @InjectMocks
    private ConsoleService consoleService;

    @Mock
    private ConsoleInterface consoleInterface;

    @Mock
    private MessageService messageService;

    @Mock
    private BoardService boardService;

    @Mock
    private BoardColumnService boardColumnService;

    @Mock
    private CardService cardService;

    @Mock
    private ExcelExporter excelExporter;

    @Mock
    private PDFExporter pdfExporter;

    @Test
    void shouldCreateNewBoardWhenValidInputProvided() {
        // given
        Board mockBoard = new Board();
        List<BoardColumn> mockColumns = List.of(new BoardColumn());
        List<Card> mockCards = List.of(new Card());
        // when
        Mockito.when(messageService.getMessage("type.board.name")).thenReturn("type.board.name");
        Mockito.when(messageService.getMessage("type.columns.quantity")).thenReturn("type.columns.quantity");
        Mockito.when(messageService.getMessage("type.cards.quantity")).thenReturn("type.cards.quantity");
        Mockito.when(messageService.getMessage("type.card.title")).thenReturn("type.card.title");
        Mockito.when(messageService.getMessage("type.card.description")).thenReturn("type.card.description");
        Mockito.when(consoleInterface.prompt("type.board.name")).thenReturn("My Board");
        Mockito.when(consoleInterface.prompt("type.columns.quantity")).thenReturn("3");
        Mockito.when(consoleInterface.prompt("type.cards.quantity")).thenReturn("1");
        Mockito.when(consoleInterface.prompt("type.card.title")).thenReturn("Card 1");
        Mockito.when(consoleInterface.prompt("type.card.description")).thenReturn("Description 1");
        Mockito.when(boardService.addBoard("My Board")).thenReturn(mockBoard);
        Mockito.when(boardColumnService.generateBoardColumns(3, mockBoard)).thenReturn(mockColumns);
        Mockito.when(cardService.generateCards(Mockito.anyMap(), Mockito.any())).thenReturn(mockCards);
        consoleService.createNewBoard();
        // then
        verify(boardService).save(mockBoard);
    }
    @Test
    void shouldDeleteBoardWhenBoardExists() {
        // given
        Board board = new Board();
        Optional<Board> boardOpt = Optional.of(board);
        // when
        Mockito.when(messageService.getMessage("type.board.id.or.name")).thenReturn("type.board.id.or.name");
        Mockito.when(consoleInterface.prompt("type.board.id.or.name")).thenReturn("1");
        Mockito.when(boardService.existsByIdOrNameIgnoreCase(1L, "")).thenReturn(true);
        Mockito.when(boardService.findByIdOrNameIgnoreCase(1L, "")).thenReturn(boardOpt);
        consoleService.deleteBoard();
        // then
        verify(boardService).delete(board);
    }
    @Test
    void shouldExportMovementReportToExcelWhenOptionIs1() {
        //given
        Board board = new Board();
        board.setId(1L);
        List<MovementReport> reportList = List.of();
        //when
        Mockito.when(consoleInterface.prompt(Mockito.anyString()))
                .thenReturn("1") // board ID
                .thenReturn("1"); // export as Excel
        Mockito.when(messageService.getMessage(Mockito.anyString())).thenReturn("Mensagem");
        Mockito.when(boardService.existsByIdOrNameIgnoreCase(1L, "")).thenReturn(true);
        Mockito.when(boardService.findByIdOrNameIgnoreCase(1L, "")).thenReturn(Optional.of(board));
        Mockito.when(cardService.getMovementReport(1L)).thenReturn(reportList);
        consoleService.printMovementReport();
        //then
        Mockito.verify(excelExporter).exportMovementToExcel(reportList,
                "C:\\Users\\GabrielAlves\\Desktop\\MovementReport.xlsx",
                "MovementReport");
    }
    @Test
    void shouldExportBlockedReportToExcelWhenOptionIs1() {
        //given
        Board board = new Board();
        board.setId(1L);
        List<BlockUnblockReport> blockedReports = List.of(Mockito.mock(BlockUnblockReport.class));
        //when
        Mockito.when(messageService.getMessage("type.board.id.or.name")).thenReturn("type.board.id.or.name");
        Mockito.when(messageService.getMessage("type.report.type")).thenReturn("type.report.type");
        Mockito.when(consoleInterface.prompt("type.board.id.or.name")).thenReturn("1");
        Mockito.when(consoleInterface.prompt("type.report.type")).thenReturn("1");
        Mockito.when(boardService.existsByIdOrNameIgnoreCase(1L, "")).thenReturn(true);
        Mockito.when(boardService.findByIdOrNameIgnoreCase(1L, "")).thenReturn(Optional.of(board));
        Mockito.when(cardService.getBlockedReport(1L)).thenReturn(blockedReports);
        consoleService.printBlockedReport();
        //then
        verify(excelExporter).exportBlockedUnblockedToExcel(
                Mockito.eq(blockedReports),
                Mockito.contains("blockReport.xlsx"),
                Mockito.eq("blockReport")
        );
    }
    @Test
    void shouldExportBlockedReportToPdfWhenOptionIs2() {
        //given
        Board board = new Board();
        board.setId(1L);
        List<BlockUnblockReport> reportList = List.of();
        //when
        Mockito.when(consoleInterface.prompt(Mockito.anyString()))
                .thenReturn("1") // board ID
                .thenReturn("2"); // export as PDF
        Mockito.when(messageService.getMessage(Mockito.anyString())).thenReturn("Mensagem");
        Mockito.when(boardService.existsByIdOrNameIgnoreCase(1L, "")).thenReturn(true);
        Mockito.when(boardService.findByIdOrNameIgnoreCase(1L, "")).thenReturn(Optional.of(board));
        Mockito.when(cardService.getBlockedReport(1L)).thenReturn(reportList);
        consoleService.printBlockedReport();
        //then
        Mockito.verify(pdfExporter).exportBlockedUnblockedReportToPDF(reportList, "C:\\Users\\GabrielAlves\\Desktop\\blockReport.pdf", "blockReport");
    }
    @Test
    void shouldExportMovementReportToPdfWhenOptionIs2() {
        //given
        Board board = new Board();
        board.setId(1L);
        List<MovementReport> movementReports = List.of(Mockito.mock(MovementReport.class));
        //when
        Mockito.when(messageService.getMessage("type.board.id.or.name")).thenReturn("type.board.id.or.name");
        Mockito.when(messageService.getMessage("type.report.type")).thenReturn("type.report.type");
        Mockito.when(consoleInterface.prompt("type.board.id.or.name")).thenReturn("1");
        Mockito.when(consoleInterface.prompt("type.report.type")).thenReturn("2");
        Mockito.when(boardService.existsByIdOrNameIgnoreCase(1L, "")).thenReturn(true);
        Mockito.when(boardService.findByIdOrNameIgnoreCase(1L, "")).thenReturn(Optional.of(board));
        Mockito.when(cardService.getMovementReport(1L)).thenReturn(movementReports);
        consoleService.printMovementReport();
        //then
        verify(pdfExporter).exportMovementReportToPDF(
                Mockito.eq(movementReports),
                Mockito.contains("MovementReport.pdf"),
                Mockito.eq("MovementReport")
        );
    }
    @Test
    void shouldCancelCardIfCardIsNotCancelled (){
        //given
        List<BoardColumn> boardColumnList = new ArrayList<>();
        BoardColumn pendingColumn = new BoardColumn();
        BoardColumn cancelledColumn = new BoardColumn();
        pendingColumn.setId(1L);
        pendingColumn.setName("pending");
        pendingColumn.setBoard_order(0);
        cancelledColumn.setBoard_order(1);
        cancelledColumn.setId(2L);
        cancelledColumn.setName("cancelled");
        boardColumnList.add(0, pendingColumn);
        boardColumnList.add(1, cancelledColumn);
        Card selectedCard = new Card("title","desc", OffsetDateTime.now(),OffsetDateTime.now(),false,pendingColumn);
        //when
        consoleService.cancelCard(boardColumnList,selectedCard);
        // then
        assertEquals("cancelled", selectedCard.getBoardColumn().getName());
        assertFalse(selectedCard.getIsBlocked());
        verify(cardService).save(Mockito.any(Card.class));
        verify(consoleInterface).printMessage(messageService.getMessage("msg.card.cancelled"));
    }
    @Test
    void shouldNotCancelCardIfCardIsAlreadyCancelled (){
        //given
        List<BoardColumn> boardColumnList = new ArrayList<>();
        BoardColumn pendingColumn = new BoardColumn();
        BoardColumn cancelledColumn = new BoardColumn();
        pendingColumn.setId(1L);
        pendingColumn.setName("pending");
        pendingColumn.setBoard_order(0);
        cancelledColumn.setBoard_order(1);
        cancelledColumn.setId(2L);
        cancelledColumn.setName("cancelled");
        boardColumnList.add(0, pendingColumn);
        boardColumnList.add(1, cancelledColumn);
        Card selectedCard = new Card("title", "desc", OffsetDateTime.now(), OffsetDateTime.now(),false, cancelledColumn);
        //when
        consoleService.cancelCard(boardColumnList,selectedCard);
        // then
        verify(consoleInterface).printMessage(messageService.getMessage("msg.error.card.already.cancelled"));
        verify(cardService, Mockito.never()).save(Mockito.any(Card.class));
        verifyNoMoreInteractions(cardService);

    }
    @Test
    void shouldNotMoveCardIfCardIsCancelled (){
        //given
        List<BoardColumn> boardColumnList = new ArrayList<>();
        BoardColumn pendingColumn = new BoardColumn();
        BoardColumn cancelledColumn = new BoardColumn();
        pendingColumn.setId(1L);
        pendingColumn.setName("pending");
        pendingColumn.setBoard_order(0);
        cancelledColumn.setBoard_order(1);
        cancelledColumn.setId(2L);
        cancelledColumn.setName("cancelled");
        boardColumnList.add(0, pendingColumn);
        boardColumnList.add(1, cancelledColumn);
        Card selectedCard = new Card("title","desc", OffsetDateTime.now(),OffsetDateTime.now(),true,pendingColumn);
        //when
        consoleService.moveCardToNextColumn(boardColumnList,selectedCard);
        // then
        assertEquals("pending", selectedCard.getBoardColumn().getName());
        assertTrue(selectedCard.getIsBlocked());
    }
    @Test
    void shouldMoveCardIfCardIsNotCancelled (){
        //given
        List<BoardColumn> boardColumnList = new ArrayList<>();
        BoardColumn initialColumn = new BoardColumn();
        BoardColumn pendingColumn = new BoardColumn();
        BoardColumn cancelledColumn = new BoardColumn();
        initialColumn.setId(1L);
        initialColumn.setName("initial");
        initialColumn.setBoard_order(0);

        pendingColumn.setId(2L);
        pendingColumn.setName("pending");
        pendingColumn.setBoard_order(1);

        cancelledColumn.setId(3L);
        cancelledColumn.setName("cancelled");
        cancelledColumn.setBoard_order(2);

        boardColumnList.add(0, initialColumn);
        boardColumnList.add(1, pendingColumn);
        boardColumnList.add(2, cancelledColumn);
        Card selectedCard = new Card("title","desc", OffsetDateTime.now(),OffsetDateTime.now(),false,initialColumn);
        //when
        consoleService.moveCardToNextColumn(boardColumnList,selectedCard);
        //then
        assertEquals("pending", selectedCard.getBoardColumn().getName());
    }
    @Test
    void shouldBlockCardIfNotBlocked() {
        // given
        Card selectedCard = new Card("title", "desc", OffsetDateTime.now(), null, false, new BoardColumn());
        // when
        Mockito.when(messageService.getMessage("type.block.reason")).thenReturn("block.reason");
        Mockito.when(consoleInterface.prompt("block.reason")).thenReturn("reason");
        consoleService.blockCard(selectedCard);
        // then
        assertTrue(selectedCard.getIsBlocked());
        assertEquals("reason", selectedCard.getBlockedReason());
        assertNull(selectedCard.getUnblockedReason());
        assertNotNull(selectedCard.getLastBlockedDt());
        assertNotNull(selectedCard.getUpdateDt());
        verify(cardService).save(selectedCard);
    }
    @Test
    void shouldNotBlockCardIfBlocked() {
        // given
        Card selectedCard = new Card("title", "desc", OffsetDateTime.now(), null, true, new BoardColumn());
        // when
        Mockito.when(messageService.getMessage("msg.error.card.already.blocked")).thenReturn("msg.error.card.already.blocked");
        consoleService.blockCard(selectedCard);
        // then
        verify(consoleInterface).printMessage("msg.error.card.already.blocked");
        verify(cardService, Mockito.never()).save(Mockito.any());
    }
    @Test
    void shouldUnblockCardIfBlocked() {
        // given
        Card selectedCard = new Card("title", "desc", OffsetDateTime.now(), null, true, new BoardColumn());
        selectedCard.setBlockedReason("previous reason");
        selectedCard.setLastBlockedDt(OffsetDateTime.now());
        // when
        Mockito.when(messageService.getMessage("type.unblock.reason")).thenReturn("unblock.reason");
        Mockito.when(consoleInterface.prompt("unblock.reason")).thenReturn("unblocked now");
        consoleService.unblockCard(selectedCard);
        // then
        assertFalse(selectedCard.getIsBlocked());
        assertNull(selectedCard.getBlockedReason());
        assertNull(selectedCard.getLastBlockedDt());
        assertEquals("unblocked now", selectedCard.getUnblockedReason());
        assertNotNull(selectedCard.getUpdateDt());
        assertNotNull(selectedCard.getLastUnblockedDt());
        verify(cardService).save(selectedCard);
    }
    @Test
    void shouldNotUnblockIfNotBlocked() {
        // given
        Card selectedCard = new Card("title", "desc", OffsetDateTime.now(), null, false, new BoardColumn());
        // when
        Mockito.when(messageService.getMessage("msg.error.impossible.do.unblock"))
                .thenReturn("cannot.unblock");
        consoleService.unblockCard(selectedCard);
        // then
        verify(consoleInterface).printMessage("cannot.unblock");
        verify(cardService, Mockito.never()).save(Mockito.any());
    }
}

