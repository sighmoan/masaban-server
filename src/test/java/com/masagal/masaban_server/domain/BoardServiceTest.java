package com.masagal.masaban_server.domain;

import com.masagal.masaban_server.model.Board;
import com.masagal.masaban_server.model.BoardRepository;
import com.masagal.masaban_server.model.CardRepository;
import com.masagal.masaban_server.model.ColumnRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BoardServiceTest {

    BoardService boardService;
    Board mockBoard = mock(Board.class);
    UUID useUuid = UUID.randomUUID();

    @BeforeEach
    void setup() {
        boardService = new BoardService(mock(BoardRepository.class), mock(ColumnRepository.class), mock(CardRepository.class));
        when(mockBoard.getId()).thenReturn(useUuid);
        when(mockBoard.getColumnLabels())
                .thenReturn(new String[]{"To-do", "Doing", "Done"});
    }

    @Test
    void canAddBoard() {
        //Arrange
        //Act
        //Assert
        assertDoesNotThrow(() -> boardService.addBoard(mockBoard));
    }

    @Test
    void canGetBoard() {
        //Arrange
        UUID boardId = boardService.createBoard();
        //Act
        //Assert
        assertNotNull(boardService.getBoard(boardId));
    }

}