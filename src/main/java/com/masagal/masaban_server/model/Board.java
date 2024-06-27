package com.masagal.masaban_server.model;

import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Transient
    private final Logger logger = LogManager.getLogger();


    @OneToMany
    private List<Column> columns;


    public Board() {
    }

    public UUID getId() {
        return this.id;
    }
    public void setId(UUID id) { this.id = id; }

    public void add(Card card) {
        columns.getFirst().add(card);
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public void setColumns(ArrayList<Column> columns) {
        this.columns = columns;
    }


    public Card createCard(String contents) {
        Card card = new Card(contents, UUID.randomUUID());
        add(card);
        logger.debug("created card with id {} and contents {}", card.getId(), card.getText());
        logger.debug("board with id "+getId());
        return card;
    }

    /*public Card getCardById(UUID cardId) {
        Optional<Card> result = cards.stream().filter((x) -> x.id().equals(cardId)).findFirst();
        logger.trace(result.toString());
        if(result.isEmpty()) {
            logger.error("Card requested with id {} does not exist", cardId);

            throw new IllegalArgumentException("Card with id does not exist: "+cardId);
        }
        return result.get();
    }*/

    public Card updateCard(UUID id, String contents) {
        /*Card newCard = new Card(contents, id);
        deleteCard(id);
        cards.add(newCard);
        return newCard;*/
        return null;
    }

    public Card updateCard(Card card, String newData) {
        return null;
    }

    public boolean deleteCard(UUID id) {
        /*Card deletedCard = getCardById(id);
        cards.remove(deletedCard);*/
        return false;
    }

    public boolean deleteCard(Card card) {
        return false;
    }

    private Column getColumn(int index) {
        return columns.get(index);
    }

    public String[] getColumnLabels() {
        //return columns.stream().map(Column::name).toArray(String[]::new);
        return null;
    }

    public String getColumnLabel(int index) {
        //return getColumn(index).name();
        return null;
    }

    private Board addColumn(String text, int location, ArrayList<Card> cardArray) {
        //Column newColumn = new Column(text, cardArray);
        //columns.add(location, newColumn);
        return this;
    }

    public Board addColumn(String text, int location) {
        return addColumn(text, location, new ArrayList<Card>());
    }

    public Board addColumn(Column col) {
        columns.add(col);
        return this;
    }

    public Board renameColumn(String newText, int location) {
        Column oldColumn = getColumn(location);
        //addColumn(newText, location, oldColumn.cardArray());
        return removeColumn(oldColumn);
    }

    private Board removeColumn(Column column) {
        columns.remove(column);
        return this;
    }

    public Board removeColumn(int location) {
        return removeColumn(getColumn(location));
    }

    public List<Card> getCardsInColumn(int columnNumber) {
        //return getColumn(columnNumber).cardArray();
        return null;
    }

    public void moveCardToColumn(Card cardToMove, int columnNumber) {
        //getColumn(columnNumber).cardArray().add(cardToMove);
    }

    public void addColumnAtIndex(Column col, int columnIndex) {
        /*

        HERE IS A BAD HACK
        DO NOT FUCK AROUND WITH THE INDEX LIKE THAT

         */
        this.columns.add(0, col);
    }
}
