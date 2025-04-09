package com.gag.board.service;

import com.gag.board.entity.Board;
import com.gag.board.entity.BoardColumn;
import com.gag.board.repository.BoardColumnRepository;
import com.gag.board.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoardColumnServiceTest {

    @Mock
    BoardColumnRepository boardColumnRepository;

    @InjectMocks
    BoardColumnService boardColumnService;
@Test
    void shouldReturnListOfBoardColumnsByBoardIdWhenIdExists() {
        // given
        List<BoardColumn> boardColumnListMock = new ArrayList<>(1);
        boardColumnListMock.addFirst(new BoardColumn());
        Mockito.when(boardColumnRepository.findByBoard_Id(1L))
                .thenReturn(boardColumnListMock);
        // when
        List<BoardColumn> boardColumnList = boardColumnService.findByBoard_Id(1L);
        // then
        assertNotNull(boardColumnList);
        assertEquals(1,boardColumnList.size());
    }
    @Test
    void shouldReturnNewListOfBoardColumns() {
        // given
        Board board = new Board();
        board.setId(1L);
        // when
        List<BoardColumn> boardColumns = boardColumnService.generateBoardColumns(3, board);
        // then
        assertNotNull(boardColumns);
        assertEquals(3, boardColumns.size());
    }

}
