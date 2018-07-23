package com.flashcards.android.flashcards.lib.model;

import java.util.List;

/**
 * Created by Abdullah Ali on 22/07/2018
 */
public class SimpleDeck {
    private String name;
    private List<SimpleCard> cards;

    public SimpleDeck(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SimpleCard> getCards() {
        return cards;
    }

    public void setCards(List<SimpleCard> cards) {
        this.cards = cards;
    }
}
