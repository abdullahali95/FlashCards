package com.flashcards.android.flashcards.lib.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
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

    public boolean addCard (Card card) {
        cards.add(card);
        return true;
    }


//    /**
//     * This method applies a modified version of SuperMemo 2 algorithm
//     * to determine the interval to next revision session.
//     * This methods appropriately sets the dateLastUsed, nextTestDue, and increments interval
//     *
//     */
//    public void setDates() {
//        Date date = null;
//        Calendar dateLastUsed = Calendar.getInstance();
//        Calendar nextDue = Calendar.getInstance();
//        Calendar now = Calendar.getInstance();
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
//
//        try {
//            date = sf.parse(lastUsed);
//            dateLastUsed.setTime(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        // After first revision, set the next revision time for 1 day later
//        if (interval == 0) {
//            interval++;
//            ef = 2.5;
//            setLastUsed(sf.format(now.getTime()));
//            nextDue = now;
//            nextDue.add(Calendar.MINUTE, 24);
//            setNextTestDue(sf.format(nextDue.getTime()));
//            Log.d("3 ", "setDates: ");
//
//            return;
//
//        } else {
//
//            try {
//                date = sf.parse(nextTestDue);
//                nextDue.setTime(date);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            long diff = nextDue.getTimeInMillis() - dateLastUsed.getTimeInMillis();
//            // Get half of the difference in hours
//            int diffHourHalf = (int)  ((double) diff / (1000 * 3600 * 0.5));
//            Calendar half = dateLastUsed;
//            half.add(Calendar.HOUR, diffHourHalf);
//
//            // If user revises before halfway through revision, disregard it.
//            if (now.before(half)) {
//                return;
//
//                // If leitner score very low, start with smaller increments again.
//                // This condition should be checked for after the one above,
//            } else if (ls < 2.5) {
//                Log.d("2 ", "setDates: ");
//
//                interval = 1;
//                ef = 2.5;
//                setLastUsed(sf.format(now.getTime()));
//                nextDue = now;
//                nextDue.add(Calendar.MINUTE, 24);
//                setNextTestDue(sf.format(nextDue.getTime()));
//                return;
//
//                // Sets the next date to be between 1.3 - 2.5 x the interval of the last interval
//                // The interval factor is ef (Easiness Factor)
//            } else {
//                Log.d("1 ", "setDates: ");
//                interval++;
//                if (ef == 0) ef = 2.5;      //Initialise ef at 2.5
//                ef = ef - 0.8 + (0.28*ls) - (0.02*ls*ls);
//                Log.d(String.valueOf(ef), "setDatesEF: ");
//
//                if (ef < 1.3) ef = 1.3;     // ef shouldn't fall below 1.3
//
//                setLastUsed(sf.format(now.getTime()));
//                nextDue = now;
//                int hours = (int) Math.round(diffHourHalf * 2 * ef);
//                //TODO: FIX THIS! returning 0!!!
//                Log.d(String.valueOf(hours), "setDatesHours: ");
//
//                nextDue.add(Calendar.HOUR, hours);
//                setNextTestDue(sf.format(nextDue.getTime()));
//                Log.d("now setDates: ", sf.format(now.getTime()));
//                Log.d("nextDue setDates: ", sf.format(nextDue.getTime()));
//
//                return;
//            }
//        }
//
//    }

    /**
     * This method applies a modified version of SuperMemo 2 algorithm
     * to determine the interval to next revision session.
     *
     */
    public void setNextInterval () {
        if (interval == 0) {
            // Initialising to be tested a day later.
            // Should be 24. 25 Purely so that the date for 'Revision due' is displayed as 'a day from now'
            // As opposed to '24 hours from now'
            interval = 25;
            ef = 2.5;
            Log.d("interval were 0", "setNextInterval: ");
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
            // (1000 * 60) * 2 = 120000: Conversion from ms --> minutes, halfed
            int diffHourHalf = (int)  ((double) diff / (120000));
            Calendar half = dateLastUsed;
            half.add(Calendar.MINUTE, diffHourHalf);

            Log.d("last used", lastUsed);
            Log.d("Next due", nextTestDue);
            Log.d("half", (sf.format(half.getTime())));
            Log.d("now", (sf.format(now.getTime())));

            if (now.before(half)) {
                Log.d("before half", "setNextInterval: ");
                // Updates the interval with the new EF, but doesn't exponentially grow the interval to next session.
                double efNew = ef - 0.8 + (0.28*ls) - (0.02*ls*ls);
                double efDiff = efNew / ef;
                Log.d("Reps before: ", String.valueOf(interval));

                interval = (int) (interval * efDiff);
                Log.d("Reps After: ", String.valueOf(interval));
                if (interval <= 25) {
                    interval = 25;
                    setDates();
                    return;
                }

                setDates();
                ef = efNew;
                return;

            // TODO: this should only apply if EF has decreased.
            } else if (ls < 2) {
                Log.d("ls < 2", "setNextInterval: ");
                double efNew = ef - 0.8 + (0.28*ls) - (0.02*ls*ls);

                // If ls is low but EF is increasing (person learning the deck, this shouldn't reset)
                if (efNew < ef) {
                    interval = 25;
                    setDates();
                }
            } else {
                Log.d("got to last stage", "setNextInterval: ");
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
        nextDue.add(Calendar.MINUTE, interval);
        nextTestDue = (sf.format(nextDue.getTime()));

        Log.d("ef: ", String.valueOf(ef));
        Log.d("interval: ", String.valueOf(interval));
        Log.d("lastUsed: ", String.valueOf(lastUsed));
        Log.d("nextTestDue: ", String.valueOf(nextTestDue));

    }


}