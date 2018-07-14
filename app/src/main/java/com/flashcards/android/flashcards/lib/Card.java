package com.flashcards.android.flashcards.lib;

//import android.support.annotation.NonNull;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Comparable<Card>, Parcelable {
    private int id;
    private String deckId;
    private String question;
    private String answer;
    private Progress progress;

    public Card(int id, String deckId, String question, String answer) {
        this.id = id;
        this.deckId = deckId;
        this.question = question;
        this.answer = answer;
        this.progress = new Progress();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
            return ((Integer) this.id).equals(((Card) o).getId());


        }
    }


    @Override
    public String toString() {
        return "Question='" + question + '\'' +
                ", Answer='" + answer + '\'';
    }

    protected Card(Parcel in) {
        id = in.readInt();
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
        dest.writeInt(id);
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