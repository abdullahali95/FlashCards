package com.flashcards.android.flashcards.lib;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Deck implements Parcelable {
    private String name;
    private String created;
    private String lastUsed;
    private Stats stats;
    private ArrayList<Card> cards;

    public Deck(String name) {
        this.name = name;
        cards = new ArrayList<Card>();
        initialiseDates();
        stats = new Stats();

        //TODO (1): initialise other variables. Dates as current dates etc
    }

    private void initialiseDates() {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = sf.format(cal.getTime());
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

    public int getSize() {
        return cards.size();
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

    protected Deck(Parcel in) {
        name = in.readString();
        created = in.readString();
        lastUsed = in.readString();
        stats = (Stats) in.readValue(Stats.class.getClassLoader());
        if (in.readByte() == 0x01) {
            cards = new ArrayList<Card>();
            in.readList(cards, Card.class.getClassLoader());
        } else {
            cards = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(created);
        dest.writeString(lastUsed);
        dest.writeValue(stats);
        if (cards == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(cards);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Deck> CREATOR = new Parcelable.Creator<Deck>() {
        @Override
        public Deck createFromParcel(Parcel in) {
            return new Deck(in);
        }

        @Override
        public Deck[] newArray(int size) {
            return new Deck[size];
        }
    };
}