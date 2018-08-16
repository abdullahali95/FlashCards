package com.flashcards.android.flashcards.lib.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * A simpler version of Deck objects, used for importing/exporting cards and decks
 * Created by Abdullah Ali
 */

@Entity
public class Deck {
    @PrimaryKey
    @NonNull
    private String deckId;
    private String name;
    private int deckSize;

    private String created;
    private String lastUsed;
    private String nextTestDue;
    private int interval;       // Amount of times the deck is tested
    private double ls;       // Average Leitner Score from last revision session, modified to be between 0 - 5
    private double ef;      // Easiness factor

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
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        Calendar cal = Calendar.getInstance();
        String date = sf.format(cal.getTime());
        created = date;
        lastUsed = date;
    }

    @NonNull
    public String getDeckId() { return deckId; }

    public void setDeckId(@NonNull String deckId) { this.deckId = deckId; }

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

    public String getNextTestDue() {
        return nextTestDue;
    }

    public void setNextTestDue(String nextTestDue) {
        this.nextTestDue = nextTestDue;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public double getEf() {
        return ef;
    }

    public void setEf(double ef) {
        this.ef = ef;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }

    public double getLs() {
        return ls;
    }

    public void setLs(double ls) {
        this.ls = ls;
    }

    public void addCard (Card card) {
        cards.add(card);
    }



    /**
     * This method applies a modified version of SuperMemo 2 algorithm
     * to determine the interval to next revision session.
     *
     */
    public void setNextInterval () {
        if (interval == 0) {    // If the deck has not been revised before
            // Initialising to be tested a day later.
            interval = 24;
            ef = 2.5;
            setDates();
        } else {
            Date date = null;
            Calendar dateLastUsed = Calendar.getInstance();
            Calendar nextDue = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

            try {
                date = sf.parse(lastUsed);
                dateLastUsed.setTime(date);
                date = sf.parse(nextTestDue);
                nextDue.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long diff = nextDue.getTimeInMillis() - dateLastUsed.getTimeInMillis();

            // Get half of the difference in hours
            // (1000 * 60 * 60) * 2 = 7200000: Conversion from ms --> hours, halfed
            int diffHourHalf = (int)  ((double) diff / (7200000));
            Calendar half = dateLastUsed;
            half.add(Calendar.HOUR, diffHourHalf);

            if (now.before(half)) {
                // If the deck is revised more then half the time before it was due to be revised.
                // Updates the interval with the new EF, but doesn't exponentially grow the interval to next session.
                double efNew = ef - 0.8 + (0.28*ls) - (0.02*ls*ls);
                double efDiff = efNew / ef;

                interval = (int) (interval * efDiff);
                if (interval <= 24) {
                    interval = 24;
                    setDates();
                    return;
                }

                setDates();
                ef = efNew;

            } else if (ls < 2) {
                // If the deck's Learnt Score falls too low,
                // the interval to repeat revision resets to 24 hours
                double efNew = ef - 0.8 + (0.28*ls) - (0.02*ls*ls);

                // If ls is low but EF is increasing (person learning the deck, this shouldn't reset)
                if (efNew < ef) {
                    interval = 24;
                    setDates();
                }
            } else {
                // Grows the interval based on EF, calculated using the Average Learnt Score
                // EF stays between 1.3 - 2.5
                if (ef == 0) ef = 2.5;      //Initialise ef at 2.5
                ef = ef - 0.8 + (0.28*ls) - (0.02*ls*ls);
                if (ef < 1.3) ef = 1.3;     // ef shouldn't fall below 1.3

                interval = (int) (interval * ef);
                setDates();
            }


        }
    }

    /**
     * This is a helper method for setNextInterval Method
     * This methods appropriately sets the dateLastUsed, nextTestDue, and increments interval
     */
    private void setDates() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        lastUsed = (sf.format(Calendar.getInstance().getTime()));

        Calendar nextDue = Calendar.getInstance();
        nextDue.add(Calendar.HOUR, interval);
        nextTestDue = (sf.format(nextDue.getTime()));

    }


}