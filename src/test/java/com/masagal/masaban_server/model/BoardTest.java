package com.masagal.masaban_server.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
    void canRenameColumn() {
        //Arrange
        board.addColumn("First column", 0);
        board.renameColumn("Third column", 0);
        //Act
        //Assert
        assertEquals("Third column", board.getColumnLabel(0));
    }

    @Test
    void cannotGetColumnOutOfBounds() {
        //Arrange
        board.addColumn("First", 0);
        //Act
        //Assert
        assertThrows(IndexOutOfBoundsException.class, () -> board.addColumn("Second", 3));
    }

    @Test
    void canDeleteCardByReference() {
        //Arrange
        Card card = board.createCard("hello");
        //Act
        //Assert
        board.deleteCard(card);
    }

    @Test
    void canDeleteCardById() {
        //Arrange
        Card card = board.createCard("hello");
        System.out.println("card.id() = " + card.id());
        //Act
        //Assert
        board.deleteCard(card.id());
    }
}