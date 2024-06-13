package com.masagal.masaban_server.domain;

import com.masagal.masaban_server.model.Board;
import org.junit.jupiter.api.BeforeEach;
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
        boardService = new BoardService();
        when(mockBoard.getId()).thenReturn(useUuid);
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