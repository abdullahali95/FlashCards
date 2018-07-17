package com.flashcards.android.flashcards.lib;

//import android.support.annotation.NonNull;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = Deck.class,
        parentColumns = "deckId",
        childColumns = "deckId",
        onDelete = CASCADE))
public class Card implements Comparable<Card>, Parcelable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int cardId;
    @NonNull
    private String deckId;
    private String question;
    private String answer;

    @Ignore         //TODO: check if this is the best way to handle this
    private Progress progress;

    /**
     * A class to store Flash cards objects.
     * A corresponding Progress Object must be setup after a Card is created.
     * @param deckId
     * @param question
     * @param answer
     *
     */
    public Card(String deckId, String question, String answer) {
        this.deckId = deckId;
        this.question = question;
        this.answer = answer;
    }

    /**
     * For creating an empty card
     * @param deckId
     */
    @Ignore
    public Card (String deckId) {
        this.deckId = deckId;
        this.question = "";
        this.answer = "";
    }

    //TODO: progress has to be manually set once the card is created

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }


    //TODO: test the comparator

    /** Comparator for ranking cards by 'learnt score'
     *
     * @param o Card being compared
     * @return integer corresponding to the comparison of the two cards
     */
    @Override
    public int compareTo(Card o) {
        Integer thisScore = this.progress.getLearntScore();
        Integer otherScore = o.progress.getLearntScore();

        return thisScore.compareTo(otherScore);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else {
            return ((Integer) this.cardId).equals(((Card) o).getCardId());


        }
    }


    @Override
    public String toString() {
        return "Question='" + question + '\'' +
                ", Answer='" + answer + '\'';
    }

    protected Card(Parcel in) {
        cardId = in.readInt();
        question = in.readString();
        answer = in.readString();
        progress = (Progress) in.readValue(Progress.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cardId);
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeValue(progress);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}