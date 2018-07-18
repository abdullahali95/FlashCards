package com.flashcards.android.flashcards.data;

import android.arch.lifecycle.LiveData;

import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.CardsDAO;
import com.flashcards.android.flashcards.lib.Progress;
import com.flashcards.android.flashcards.lib.ProgressDAO;
import com.google.common.collect.EvictingQueue;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Abdullah Ali on 15/07/2018
 */
public class TestRepo {
    private final CardsDAO cardsDAO;
    private final ProgressDAO progressDAO;

    @Inject
    public TestRepo(CardsDAO cardsDAO, ProgressDAO progressDAO) {
        this.cardsDAO = cardsDAO;
        this.progressDAO = progressDAO;
    }

    /**
     * These need to have their Progress object also added
     * @param deckId
     * @return
     */
    public LiveData<List<Card>> getAllCards (String deckId) {
        return cardsDAO.getAllCards(deckId);
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

    public LiveData<List<Progress>> getAllProgress (String deckId) {
        return progressDAO.getAllProgress(deckId);
    }

    public LiveData<Progress> getProgress (int cardId, String deckId) {
        return progressDAO.getProgress(cardId, deckId);
    }

    public void incAttempts(int cardId, String deckId) {
        progressDAO.incAttempts(cardId, deckId);
    }

    public void incCorrect(int cardId, String deckId) {
        progressDAO.incCorrect(cardId, deckId);
    }

    //TODO: fix evicting queue
//    public void setLastTen(int cardId, String deckId, EvictingQueue<Boolean> lastTen) {
//        progressDAO.setLastTen(cardId, deckId, lastTen);
//    }
//
//    public void setLearntScore(int cardId, String deckId, int learntScore) {
//        progressDAO.setLearntScore(cardId, deckId, learntScore);
//    }
}