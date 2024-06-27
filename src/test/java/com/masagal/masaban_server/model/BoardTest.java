package com.masagal.masaban_server.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //only because for some reason cannot otherwise set log level
// TODO: figure out how to otherwise set log level
class BoardTest {
    Board board = new Board();

    @Nested
    public class ColumnFunctionalityTests {
        @Test
        void hasColumnsByDefault() {
            assertNotEquals(0, board.getColumnLabels().length);
        }

        @Test
        void canGetCardsInColumn() {
            //Arrange
            Card card1 = board.createCard("this is card1");
            Card card2 = board.createCard("this is a card2");
            Card card3 = board.createCard("card3 this is");
            Card card4 = board.createCard("YIPPEEEEKAAAAYAAAAY MOFUGGA");

            //Act
            board.addColumn("To do", 0);
            board.addColumn("Doing", 1);

            board.moveCardToColumn(card1, 1);
            board.moveCardToColumn(card2, 1);
            board.moveCardToColumn(card3, 1);

            board.addColumn("Done", 2);

            List<Card> cardsListColumn = board.getCardsInColumn(1);

            //Assert
            assertEquals(3, cardsListColumn.size());
            assertTrue(cardsListColumn.contains(card1));
        }

        @Test
        void canGetColumnByLocation() {
            //Arrange
            //Act
            board.addColumn("First column", 0);
            board.addColumn("Second column", 1);
            //Assert
            assertEquals("Second column", board.getColumnLabel(1));
            assertEquals("First column", board.getColumnLabel(0));
        }

        @Test
        void canInsertColumn() {
            board.addColumn("New column", 1);

            assertEquals("New column", board.getColumnLabel(1));
        }

        @Test
        void canRenameColumn() {
            //Arrange
            board.addColumn("First column", 0);
            board.renameColumn("Third column", 0);
            //Act
            //Assert
            assertEquals("Third column", board.getColumnLabel(0));
        }

        @Test
        void cannotAddColumnOutOfBounds() {
            //Arrange
            int numberOfColumns = board.getColumnLabels().length;
            //Act
            //Assert
            assertThrows(IndexOutOfBoundsException.class, () ->
                    board.addColumn("Second", numberOfColumns+1));
        }
    }
}