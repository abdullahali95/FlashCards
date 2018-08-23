package com.flashcards.android.flashcards.data.repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.flashcards.android.flashcards.data.FlashCardsDatabase;
import com.flashcards.android.flashcards.lib.DAO.CardsDAO;
import com.flashcards.android.flashcards.lib.DAO.DeckDAO;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;

import java.util.List;

/**
 * Created by Abdullah Ali on 16/07/2018
 *
 * This is the data repository for the Deck Information activity
 */
public class DeckInfoRepo {
    private final DeckDAO deckDAO;
    private final CardsDAO cardsDAO;

    public DeckInfoRepo(Application application) {
        FlashCardsDatabase db = FlashCardsDatabase.getFlashCardsDB(application);
        this.deckDAO = db.deckDAO();
        this.cardsDAO = db.cardsDAO();
    }


    public LiveData<Deck> getDeck(String deckId) {
        return deckDAO.getDeck(deckId);
    }

    public LiveData<List<Card>> getAllCards(String deckId) {
        return cardsDAO.getAllCards(deckId);
    }

    public Long createCard(Card card) {
        String isNull = String.valueOf((card == null));
        Log.d(isNull, "New Card is Null: ");
        return cardsDAO.createCard(card);
    }

    public int deleteCard(Card card) {
        return cardsDAO.deleteCard(card);
    }

    public int getAllCardsLength(String deckId) {
        return cardsDAO.getAllCardsLength(deckId);
    }

    public void updateDeck(Deck deck) {
        deckDAO.setName(deck.getDeckId(), deck.getName());
    }


    public void setDeckSize(String deckId, int newDeckSize) {
        deckDAO.setDeckSize(deckId, newDeckSize);
    }

    public Card getLastCard(String deckId) {
        return cardsDAO.getLastCard(deckId);
    }
}
