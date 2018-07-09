package com.flashcards.android.flashcards.lib;

class Stats {
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
}
