package com.masagal.masaban_server.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;

    @Test
    void shouldGenerateUniqueId() {
        // Arrange
        // Act
        Board firstBoard = new Board();
        Board secondBoard = new Board();
        // Assert
        assertNotEquals(firstBoard.getId(), secondBoard.getId());
    }
}