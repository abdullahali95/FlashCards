package com.flashcards.android.flashcards.lib;

import android.os.Parcel;
import android.os.Parcelable;

class Stats implements Parcelable {
    private int cards;
    private int attempts;

    public Stats() {
        this.cards = 0;
        this.attempts = 0;
    }

    public int getCards() {
        return cards;
    }

    public void setCards(int cards) {
        this.cards = cards;
    }
    public void incCards() { cards++; };

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
    public void incAttempts() { attempts++; };

    protected Stats(Parcel in) {
        cards = in.readInt();
        attempts = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cards);
        dest.writeInt(attempts);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Stats> CREATOR = new Parcelable.Creator<Stats>() {
        @Override
        public Stats createFromParcel(Parcel in) {
            return new Stats(in);
        }

        @Override
        public Stats[] newArray(int size) {
            return new Stats[size];
        }
    };
}