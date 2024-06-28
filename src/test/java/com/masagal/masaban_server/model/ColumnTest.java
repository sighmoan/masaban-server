package com.masagal.masaban_server.model;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ColumnTest {

    private Column column;

    @BeforeEach
    void setup() {
        column = new Column(new ArrayList<Card>(), "To-do");
    }

    @Test
    void canCreateCardByStringAndRetrieve() {
        //Arrange
        String textToAdd = "this is a test card";
        Card createdCard = new Card(textToAdd);
        //Act
        column.add(createdCard);
        var cards = column.getCards();
        //Assert
        assertNotNull(cards);
        assertTrue(cards.contains(createdCard));
    }

    @Test
    void canCreateAndListCards() {
        //Arrange
        Card obi = new Card("Obi-wan Kenobi");
        Card yoda = new Card("Master Yoda");
        Card luke = new Card("Luke Skywalker");
        //Act
        column.add(obi);
        column.add(yoda);
        column.add(luke);
        //Assert
        assertEquals(3, column.getCards().size());
    }

    @Test
    void canUpdateCard() {
        //Arrange
        Card card = new Card("this is the old text");
        String newText = "the new text this is";
        //Act
        column.add(card);
        card.setText(newText);
        //Assert
        assertFalse(column
                .getCards().stream()
                .filter((c) -> c.getText().equals(newText))
                .toList().isEmpty());
    }

    @Test
    void canDeleteCardByReference() {
        //Arrange
        Card card = new Card("clearance");
        column.add(card);
        //Act
        column.removeCard(card);
        //Assert
        assertTrue(column
                .getCards().stream()
                .filter((c) -> c.getText().equals("clearance"))
                .toList().isEmpty());
    }

    /*@Test
    void canDeleteCardById() {
        //Arrange
        Card card = column.createCard("hello");
        System.out.println("card.id() = " + card.id());
        //Act
        //Assert
        column.deleteCard(card.id());
    }*/
}
