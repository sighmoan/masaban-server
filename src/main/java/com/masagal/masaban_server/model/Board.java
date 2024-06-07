package com.masagal.masaban_server.model;

import com.masagal.masaban_server.services.BoardService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class Board {
    private ArrayList<Card> cards;
    private int id;
    private static int assignedIds;

    public Board() {
        cards = new ArrayList<Card>();
        this.id = ++assignedIds;
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
        return null;
    }

    public Card getCardById(int id) {
        return null;
    }

    public Card updateCard(int id, String contents) {
        return null;
    }

    public Card updateCard(Card card, String newData) {
        return null;
    }

    public boolean deleteCard(int id) {
        return false;
    }

    public boolean deleteCard(Card card) {
        return false;
    }

    public Board addColumn(String text, int location) {
        return null;
    }

    public Board renameColumn(String oldText, String newText, int location) {
        return null;
    }

    public Board removeColumn(String text, int location) {
        return null;
    }

    public List<Card> getCardsInColumn(int columnNumber) {
        return null;
    }
}
