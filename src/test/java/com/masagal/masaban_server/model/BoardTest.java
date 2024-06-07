package com.masagal.masaban_server.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
}