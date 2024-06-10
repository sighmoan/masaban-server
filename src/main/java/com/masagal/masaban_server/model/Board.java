package com.masagal.masaban_server.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class Board {
    private final Logger logger = LogManager.getLogger();
    private final ArrayList<Card> cards;
    private final int id;
    private static int assignedIds;
    private int assignedCardIds = 0;

    private record Column (String name, ArrayList<Card> cardArray) {}

    private final ArrayList<Column> columns;


    public Board() {
        cards = new ArrayList<Card>();
        this.id = assignedIds++;
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

    public int getId() {
        return this.id;
    }

    public Card createCard(String contents) {
        Card card = new Card(contents, ++assignedCardIds);
        cards.add(card);
        logger.debug("created card with id {} and contents {}", card.id(), card.text());
        return card;
    }

    public Card getCardById(int cardId) {
        Optional<Card> result = cards.stream().filter((x) -> x.id() == cardId).findFirst();
        if(result.isEmpty()) {
            logger.error("Card requested with id {} does not exist", cardId);

            throw new IllegalArgumentException("Card with id does not exist: "+cardId);
        }
        return result.get();
    }

    public Card updateCard(int id, String contents) {
        Card newCard = new Card(contents, id);
        deleteCard(id);
        cards.add(newCard);
        return newCard;
    }

    public Card updateCard(Card card, String newData) {
        return null;
    }

    public boolean deleteCard(int id) {
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
        return columns.get(index).name();
    }

    public Board addColumn(String text, int location) {
        Column newColumn = new Column(text, new ArrayList<>());
        columns.add(location, newColumn);
        return this;
    }

    public Board renameColumn(String oldText, String newText, int location) {
        return null;
    }

    public Board removeColumn(String text, int location) {
        columns.remove(getColumn(location));
        return this;
    }

    public List<Card> getCardsInColumn(int columnNumber) {
        return getColumn(columnNumber).cardArray().stream().toList();
    }

    public void moveCardToColumn(Card cardToMove, int columnNumber) {
        getColumn(columnNumber).cardArray().add(cardToMove);
    }
}
