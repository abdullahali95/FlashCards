package com.flashcards.android.flashcards.lib.model;

import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<String> deckNames;
    private ArrayList<Deck> decks;

    public User(String name) {
        this.name = name;
        this.deckNames = new ArrayList<String>();
        this.decks = new ArrayList<Deck>();
    }
}
