package com.masagal.masaban_server.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Column {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @OneToMany
    List<Card> cards;
    Integer index;
    String label;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Column(ArrayList<Card> cards, String label) {
        this.cards = cards;
        this.label = label;
    }

    public Column() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void add(Card newCard) {
        this.cards.add(newCard);
    }

    public void removeCard(Card card) {
        this.cards.remove(card);
    }
}
