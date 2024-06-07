package com.masagal.masaban_server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}