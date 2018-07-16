package com.flashcards.android.flashcards.data;

import android.arch.lifecycle.LiveData;

import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.CardsDAO;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.lib.DeckDAO;
import com.flashcards.android.flashcards.lib.Progress;
import com.flashcards.android.flashcards.lib.ProgressDAO;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Abdullah Ali on 15/07/2018
 */
public class CardEditRepo {
    private final CardsDAO cardsDAO;
    private final DeckDAO deckDAO;
    private final ProgressDAO progressDAO;

    @Inject
    public CardEditRepo(CardsDAO cardsDAO, DeckDAO deckDAO, ProgressDAO progressDAO) {
        this.cardsDAO = cardsDAO;
        this.deckDAO = deckDAO;
        this.progressDAO = progressDAO;
    }

    public void createCard(Card card) {
        cardsDAO.createCard(card);
    }

    public LiveData<Deck> getDeck (String deckId) {
        return deckDAO.getDeck(deckId);
    }

    public void updateQuestion(int cardId, String deckId, String question) {
        cardsDAO.setQuestion(cardId, deckId, question);
    }

    public void updateAnswer(int cardId, String deckId, String answer) {
        cardsDAO.setAnswer(cardId, deckId, answer);
    }

    public LiveData<List<Card>> getAllCards (String deckId) {
        return cardsDAO.getAllCards(deckId);
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

    public void addProgress (Progress progress) {
        progressDAO.addProgress(progress);
    }
}
