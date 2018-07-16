package com.flashcards.android.flashcards.lib;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

@Entity(indices = @Index(value = "uuid", name = "deckId"))
public class Deck implements Parcelable {
    @PrimaryKey
    @NonNull
    private String uuid;
    private String name;
    private String created;
    private String lastUsed;
    @Ignore
    private ArrayList<Card> cards;


    public Deck(String name) {
        this.uuid = UUID.randomUUID().toString();

        this.name = name;
        cards = new ArrayList<Card>();
        initialiseDates();

    }

    private void initialiseDates() {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String date = sf.format(cal.getTime());
        created = date;
        lastUsed = date;
    }

    public String getUuid() { return uuid; }

    public void setUuid(String uuid) { this.uuid = uuid; }

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
        return cards.size();
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

    protected Deck(Parcel in) {
        name = in.readString();
        created = in.readString();
        lastUsed = in.readString();
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