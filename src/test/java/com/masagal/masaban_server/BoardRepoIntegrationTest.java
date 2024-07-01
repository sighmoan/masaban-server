package com.masagal.masaban_server;

import com.masagal.masaban_server.domain.BoardService;
import com.masagal.masaban_server.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class BoardRepoIntegrationTest {

    @Autowired
    BoardService service;

    UUID boardId, firstColumnId;

    @BeforeEach
    void setup() {
        boardId = service.createBoard();
        firstColumnId = service.insertColumn(boardId, 0, "Zeroeth");
        service.insertColumn(boardId, 1, "One");
        service.insertColumn(boardId, 2, "Two");
        service.insertColumn(boardId, 3, "Three");
    }

    @Test
    void canAddCard() {
        service.createCardOnColumn(boardId, firstColumnId);

        assertFalse(service.getCardsByColumn(firstColumnId).isEmpty());

    }

    @Test
    void canDeleteCard() {
        service.createCardOnColumn(boardId, firstColumnId);
        UUID cardId = service.createCardOnColumn(boardId, firstColumnId);

        service.deleteCardOnBoard(boardId, cardId);
        assertEquals(1, service.getCardsByColumn(firstColumnId).size());
    }

    @Test
    void canMoveCardToAnotherColumn() {
        List<Column> cols = service.getColumns(boardId);
        service.createCardOnColumn(boardId, cols.get(0).getId());

        UUID cardId = service.createCardOnColumn(boardId, cols.get(0).getId());
        service.moveCard(cardId, cols.get(1).getId());

        assertEquals(1, service.getCardsByColumn(cols.get(0).getId()).size());
        assertEquals(1, service.getCardsByColumn(cols.get(1).getId()).size());
    }

    @Test
    void canUpdateContentOfCard() {
        fail();
    }
}
