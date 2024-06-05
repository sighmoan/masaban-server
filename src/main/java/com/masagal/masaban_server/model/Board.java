package com.masagal.masaban_server.model;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class Board {
    private ArrayList<Card> cards;

    public Board() {
        cards = new ArrayList<Card>();
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
}
