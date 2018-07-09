package com.flashcards.android.flashcards.lib;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Deck {
    private String name;
    private String created;
    private String lastUsed;
    private Stats stats;
    private ArrayList<Card> cards;

    public Deck(String name) {
        this.name = name;
        initialiseDates();
        stats = new Stats();

        //TODO (1): initialise other variables. Dates as current dates etc
    }

    private void initialiseDates() {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-YYYY");
        Calendar cal = Calendar.getInstance();
        String date = sf.format(cal);
        created = date;
        lastUsed = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated() {
        return created;
    }

    public String getLastUsed() {
        return lastUsed;
    }

    /**
     *
     * @param lastUsed: date of last use of deck. must be in the format: 'dd-MM-YYYY'
     */
    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public boolean addCard (Card card) {
        cards.add(card);
        stats.incCards();
        //TODO: update db

        return true;
    }

    public void incAttempts() {
        stats.incAttempts();
    }

    // TODO (1): Add removeCard()
}
