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

    }

    public Card getCardById(int id) {

    }

    public Card updateCard(int id, String contents) {

    }

    public Card updateCard(Card card, String newData) {

    }

    public boolean deleteCard(int id) {

    }

    public boolean deleteCard(Card card) {

    }

    public BoardService addColumn(String text, int location) {

    }

    public BoardService renameColumn(String oldText, String newText, int location) {

    }

    public BoardService removeColumn(String text, int location) {

    }

    public List<Card> getCardsInColumn(int columnNumber) {

    }
}
