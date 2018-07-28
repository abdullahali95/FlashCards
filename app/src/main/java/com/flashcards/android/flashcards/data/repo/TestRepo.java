package com.flashcards.android.flashcards.data.repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.flashcards.android.flashcards.data.FlashCardsDatabase;
import com.flashcards.android.flashcards.lib.DAO.CardsDAO;
import com.flashcards.android.flashcards.lib.DAO.DeckDAO;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;
import com.google.common.collect.EvictingQueue;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Abdullah Ali on 15/07/2018
 */
public class TestRepo {
    private final CardsDAO cardsDAO;
    private final DeckDAO deckDAO;

    @Inject
    public TestRepo(Application application) {
        FlashCardsDatabase db = FlashCardsDatabase.getFlashCardsDB(application);
        this.cardsDAO = db.cardsDAO();
        this.deckDAO = db.deckDAO();
    }

    /**
     * These need to have their Progress object also added
     * @param deckId
     * @return
     */
    public LiveData<List<Card>> getAllCards (String deckId) {
        return cardsDAO.getAllCards(deckId);
    }

    public List<Card> getAllDeckCards (String deckId) {
        return cardsDAO.getAllDeckCards(deckId);
    }

    /**
     * These need to have their Progress object also added
     * @param cardId
     * @param deckId
     * @return
     */
    public LiveData<Card> getCard (int cardId, String deckId) {
        return cardsDAO.getCard(cardId, deckId);
    }

    public void incAttempts(int cardId, String deckId) {
        cardsDAO.incAttempts(cardId, deckId);
    }

    public void incCorrect(int cardId, String deckId) {
        cardsDAO.incCorrect(cardId, deckId);
    }

    public LiveData<Deck> getDeck(String deckId) {
        return deckDAO.getDeck(deckId);
    }

    public void setDeck(Deck deck) {
        deckDAO.setDeck(deck);
    }

    public void setLastFive(int cardId, String deckId, EvictingQueue<Boolean> lastFive) {
        cardsDAO.setLastFive(cardId, deckId, lastFive);
    }

    public void setLearntScore(int cardId, String deckId, int learntScore) {
        cardsDAO.setLearntScore(cardId, deckId, learntScore);
    }

    public void setCorrect(int cardId, String deckId, int correct) {
        cardsDAO.setCorrect (cardId, deckId, correct);
    }

    public void setAttempts(int cardId, String deckId, int attempts) {
        cardsDAO.setAttempts (cardId, deckId, attempts);
    }

    public void setLastUsed(String deckId, String date) {
        deckDAO.setLastUsed(deckId, date);
    }
}
