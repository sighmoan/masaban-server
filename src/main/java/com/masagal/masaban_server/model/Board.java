package com.masagal.masaban_server.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class Board {
    private final Logger logger = LogManager.getLogger();
    private final ArrayList<Card> cards;
    private final UUID id;
    private int assignedCardIds = 0;

    private record Column (String name, ArrayList<Card> cardArray) {}

    private final ArrayList<Column> columns;


    public Board() {
        cards = new ArrayList<Card>();
        this.id = UUID.randomUUID();
        columns = new ArrayList<>();
    }

    public void add(Card newCard) {
        cards.add(newCard);
    }

    public void addMany(List<Card> list) {
        cards.addAll(list);
    }

    public List<Card> getCards() {
        return cards;
    }

    public UUID getId() {
        return this.id;
    }

    public Card createCard(String contents) {
        Card card = new Card(contents, UUID.randomUUID());
        cards.add(card);
        logger.debug("created card with id {} and contents {}", card.id(), card.text());
        logger.debug("board with id "+getId());
        return card;
    }

    public Card getCardById(UUID cardId) {
        Optional<Card> result = cards.stream().filter((x) -> x.id().equals(cardId)).findFirst();
        logger.trace(result.toString());
        if(result.isEmpty()) {
            logger.error("Card requested with id {} does not exist", cardId);

            throw new IllegalArgumentException("Card with id does not exist: "+cardId);
        }
        return result.get();
    }

    public Card updateCard(UUID id, String contents) {
        Card newCard = new Card(contents, id);
        deleteCard(id);
        cards.add(newCard);
        return newCard;
    }

    public Card updateCard(Card card, String newData) {
        return null;
    }

    public boolean deleteCard(UUID id) {
        Card deletedCard = getCardById(id);
        cards.remove(deletedCard);
        return true;
    }

    public boolean deleteCard(Card card) {
        return false;
    }

    private Column getColumn(int index) {
        return columns.get(index);
    }

    public String getColumnLabel(int index) {
        return getColumn(index).name();
    }

    private Board addColumn(String text, int location, ArrayList<Card> cardArray) {
        Column newColumn = new Column(text, cardArray);
        columns.add(location, newColumn);
        return this;
    }

    public Board addColumn(String text, int location) {
        return addColumn(text, location, new ArrayList<Card>());
    }

    public Board renameColumn(String newText, int location) {
        Column oldColumn = getColumn(location);
        addColumn(newText, location, oldColumn.cardArray());
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
        return getColumn(columnNumber).cardArray();
    }

    public void moveCardToColumn(Card cardToMove, int columnNumber) {
        getColumn(columnNumber).cardArray().add(cardToMove);
    }
}
