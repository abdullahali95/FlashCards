package com.flashcards.android.flashcards.data.repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.flashcards.android.flashcards.data.FlashCardsDatabase;
import com.flashcards.android.flashcards.lib.DAO.CardsDAO;
import com.flashcards.android.flashcards.lib.DAO.DeckDAO;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Abdullah Ali on 15/07/2018
 */
public class CardEditRepo {
    private final CardsDAO cardsDAO;
    private final DeckDAO deckDAO;

    @Inject
    public CardEditRepo(Application application) {
        FlashCardsDatabase db = FlashCardsDatabase.getFlashCardsDB(application);
        this.cardsDAO = db.cardsDAO();
        this.deckDAO = db.deckDAO();
    }

    public void createCard(Card card) {
        cardsDAO.createCard(card);
    }

    public int getAllCardsLength(String deckId) {
        return cardsDAO.getAllCardsLength(deckId);
    }

    public LiveData<Card> getCard (int cardId, String deckId) {
        return cardsDAO.getCard(cardId, deckId);
    }

    public void setCard(Card card) {
        cardsDAO.setCard(card);
    }

    public void deleteCard (Card card) {
        cardsDAO.deleteCard(card);
    }

    public void setDeckSize(String deckId, int newDeckSize) {
        deckDAO.setDeckSize(deckId, newDeckSize);
    }

    public Card getLastCard(String deckId) {
        return cardsDAO.getLastCard(deckId);
    }

}
