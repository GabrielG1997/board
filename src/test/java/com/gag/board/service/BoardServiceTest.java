package com.gag.board.service;

import com.gag.board.dto.BlockUnblockReport;
import com.gag.board.dto.MovementReport;
import com.gag.board.entity.Board;
import com.gag.board.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private CardService cardService;

    @InjectMocks
    private BoardService boardService;
    @Test
    void save_shouldCallBoardRepositorySave() {
        Board board = new Board();
        boardService.save(board);

        Mockito.verify(boardRepository).save(board);
    }
    @Test
    void delete_shouldCallBoardRepositoryDelete() {
        Board board = new Board();
        boardService.delete(board);

        Mockito.verify(boardRepository).delete(board);
    }
    @Test
    void addBoard_shouldCreateBoardWithNameAndDate() {
        String name = "Meu Quadro";
        Board board = boardService.addBoard(name);

        assertNotNull(board);
        assertEquals(name, board.getName());
        assertNotNull(board.getCreateDt());
        assertTrue(board.getCreateDt().isBefore(OffsetDateTime.now().plusSeconds(1)));
    }
    @Test
    void shouldReturnBoardWhenIdOrNameExists() {
        // given
        Board mockBoard = new Board();
        mockBoard.setId(1L);
        mockBoard.setName("My Board");

        Mockito.when(boardRepository.findByIdOrNameIgnoreCase(1L,"My Board"))
                .thenReturn(Optional.of(mockBoard));
        // when
        Optional<Board> board = boardService.findByIdOrNameIgnoreCase(1L,"My Board");
        // then
        assertNotNull(board);
        assertEquals("My Board", board.get().getName());

    }
    @Test
    void shouldReturnTrueWhenIdOrNameExists(){
        //given
        Board mockBoard = new Board();
        mockBoard.setId(1L);
        mockBoard.setName("My Board");
        // when
        Mockito.when(boardService.existsByIdOrNameIgnoreCase(mockBoard.getId(), mockBoard.getName())).thenReturn(true);
        //Then
        assertTrue(boardService.existsByIdOrNameIgnoreCase(1L, "My Board"));
    }
    @Test
    void getMovementReport_shouldDelegateToCardService() {
        long boardId = 1L;
        List<MovementReport> mockReports = List.of(new MovementReport(
                1L,
                "title",
                "description",
                "type",
                1L,
                1L,
                LocalDateTime.now(),
                5.0
        ));

        Mockito.when(cardService.getMovementReport(boardId)).thenReturn(mockReports);

        List<MovementReport> result = boardService.getMovementReport(boardId);

        assertEquals(mockReports, result);
        Mockito.verify(cardService).getMovementReport(boardId);
    }
    @Test
    void getBlockUnblockReport_shouldDelegateToCardService() {
        long boardId = 1L;
        List<BlockUnblockReport> mockReports = List.of(new BlockUnblockReport(
                1L,
                "title",
                "description",
                "type",
                1L,
                1L,
                true,
                "",
                "",
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now(),
                5L
        ));

        Mockito.when(cardService.getBlockedReport(boardId)).thenReturn(mockReports);

        List<BlockUnblockReport> result = boardService.getBlockedReport(boardId);

        assertEquals(mockReports, result);
        Mockito.verify(cardService).getBlockedReport(boardId);
    }
}
