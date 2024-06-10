package com.masagal.masaban_server.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //only because for some reason cannot otherwise set log level
// TODO: figure out how to otherwise set log level
class BoardTest {
    Board board = new Board();

    @Test
    void shouldGenerateUniqueId() {
        // Arrange
        // Act
        Board firstBoard = new Board();
        Board secondBoard = new Board();
        // Assert
        assertNotEquals(firstBoard.getId(), secondBoard.getId());
    }

    @Test
    void canCreateCardByStringAndRetrieve() {
        //Arrange
        String textToAdd = "this is a test card";
        //Act
        Card createdCard = board.createCard(textToAdd);
        Card fetchedCard = board.getCardById(createdCard.id());
        //Assert
        assertNotNull(fetchedCard);
        assertEquals(textToAdd, fetchedCard.text());
    }

    @Test
    void canCreateAndListCards() {
        //Arrange
        String obi="Obi-wan Kenobi";
        String yoda="Master Yoda";
        String luke="Luke Skywalker";
        //Act
        board.createCard(obi);
        board.createCard(yoda);
        board.createCard(luke);
        //Assert
        assertEquals(3, board.getCards().size());
    }

    @Test
    void canUpdateCard() {
        //Arrange
        String oldText = "this is the old text";
        String newText = "the new text this is";
        //Act
        Card addedCard = board.createCard(oldText);
        board.updateCard(addedCard.id(), newText);
        //Assert
        assertNotEquals(oldText, board.getCardById(addedCard.id()).text());
        assertEquals(newText, board.getCardById(addedCard.id()).text());
    }

    @Test
    void canGetCardsInColumn() {
        //Arrange
        Card card1 = board.createCard("this is card1");
        Card card2 = board.createCard("this is a card2");
        Card card3 = board.createCard("card3 this is");
        Card card4 = board.createCard("YIPPEEEEKAAAAYAAAAY MOFUGGA");

        //Act
        board.addColumn("To do", 1);
        board.addColumn("Doing", 2);

        board.moveCardToColumn(card1, 2);
        board.moveCardToColumn(card2, 2);
        board.moveCardToColumn(card3, 2);

        board.addColumn("Done", 3);

        List<Card> cardsListColumn = board.getCardsInColumn(2);

        //Assert
        assertEquals(3, cardsListColumn.size());
        assertTrue(cardsListColumn.contains(card1));
    }

    @Test
    void canGetColumnByLocation() {
        //Arrange
        //Act
        board.addColumn("First column", 1);
        board.addColumn("Third column", 3);
        //Assert
        assertEquals("Third column", board.getColumn(3).name());
        assertEquals("First column", board.getColumn(1).name());
    }

    @Test
    void canRenameColumn() {
        //Arrange
        board.addColumn("First column", 1)
                .renameColumn("First column", "Third column", 1);
        //Act
        //Assert
        assertEquals("Third column", board.getColumn(1).name());
    }
}