package com.flashcards.android.flashcards.lib;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

@Entity
public class Deck {
    @PrimaryKey
    @NonNull
    private String deckId;
    private String name;
    private String created;
    private String lastUsed;
    @Ignore
    private ArrayList<Card> cards;

    // Constructor for creating a deck in app
    @Ignore
    public Deck(String name) {
        this.deckId = UUID.randomUUID().toString();
        this.name = name;
        cards = new ArrayList<Card>();
        initialiseDates();

    }

    /**
     * Constructor for Room db to create objects
     * @param deckId
     * @param name
     * @param created
     * @param lastUsed
     */
    public Deck(@NonNull String deckId, String name, String created, String lastUsed) {
        this.deckId = deckId;
        this.name = name;
        this.created = created;
        this.lastUsed = lastUsed;
    }


    private void initialiseDates() {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = sf.format(cal.getTime());
        created = date;
        lastUsed = date;
    }

    public String getDeckId() { return deckId; }

    public void setDeckId(String deckId) { this.deckId = deckId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastUsed() {
        return lastUsed;
    }

    public int getSize() {
        if (cards == null) return 0;
        else return cards.size();
    }

    /**
     *
     * @param lastUsed: date of last use of deck. must be in the format: 'dd-MM-yyyy'
     */
    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public boolean addCard (Card card) {
        cards.add(card);
        //TODO: update db

        return true;
    }

    // TODO (1): Add removeCard()


}