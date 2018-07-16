package com.flashcards.android.flashcards.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.lib.DeckDAO;

import java.util.List;
import javax.inject.Inject;

/**
 * Created by Abdullah Ali on 15/07/2018
 */
public class MainRepo {

    private final DeckDAO deckDAO;

    public MainRepo(Application application) {
        FlashCardsDatabase db = FlashCardsDatabase.getFlashCardsDB(application);
        this.deckDAO = db.deckDAO();
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

}
