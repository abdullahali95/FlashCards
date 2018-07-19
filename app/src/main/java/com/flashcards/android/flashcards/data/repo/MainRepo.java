package com.flashcards.android.flashcards.data.repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.flashcards.android.flashcards.data.FlashCardsDatabase;
import com.flashcards.android.flashcards.lib.DAO.CardsDAO;
import com.flashcards.android.flashcards.lib.model.Deck;
import com.flashcards.android.flashcards.lib.DAO.DeckDAO;

import java.util.List;

/**
 * Created by Abdullah Ali on 15/07/2018
 */
public class MainRepo {

    private final DeckDAO deckDAO;
    private final CardsDAO cardDAO;

    public MainRepo(Application application) {
        FlashCardsDatabase db = FlashCardsDatabase.getFlashCardsDB(application);
        this.deckDAO = db.deckDAO();
        this.cardDAO = db.cardsDAO();
    }

    public LiveData<List<Deck>> getAllDecks() {
        return deckDAO.getAllDecks();
    }

    public void addDeck (Deck deck) {
        deckDAO.addDeck(deck);
    }

    public void deleteDeck (Deck deck) {
        deckDAO.deleteDeck(deck);
    }

    public LiveData<Integer> getDeckSize (String deckId) {
        return cardDAO.getDeckSize(deckId);
    }

}
