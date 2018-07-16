package com.flashcards.android.flashcards.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.CardsDAO;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.lib.DeckDAO;
import com.flashcards.android.flashcards.lib.Progress;
import com.flashcards.android.flashcards.lib.ProgressDAO;

import java.util.List;

/**
 * Created by Abdullah Ali on 16/07/2018
 */
public class DeckInfoRepo {
    private final DeckDAO deckDAO;
    private final CardsDAO cardsDAO;
    private final ProgressDAO progressDao;

    public DeckInfoRepo(Application application) {
        FlashCardsDatabase db = FlashCardsDatabase.getFlashCardsDB(application);
        this.deckDAO = db.deckDAO();
        this.cardsDAO = db.cardsDAO();
        this.progressDao = db.progressDAO();
    }


    public LiveData<Deck> getDeck(String deckId) {
        return deckDAO.getDeck(deckId);
    }

    public LiveData<List<Card>> getAllCards(String deckId) {
        return cardsDAO.getAllCards(deckId);
    }

    public void createCard(Card card) {
        String isNull = String.valueOf((card == null));
        Log.d(isNull, "New Card is Null: ");

        cardsDAO.setCard(card);
    }

}
