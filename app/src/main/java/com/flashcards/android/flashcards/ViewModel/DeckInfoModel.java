package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.flashcards.android.flashcards.data.DeckInfoRepo;
import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.CardsDAO;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.lib.DeckDAO;
import com.flashcards.android.flashcards.lib.ProgressDAO;

import java.util.List;

/**
 * Created by Abdullah Ali on 16/07/2018
 */
public class DeckInfoModel extends AndroidViewModel {
    private DeckInfoRepo repo;
    private LiveData<Deck> currentDeck;
    private LiveData<List<Card>> cards;

    public DeckInfoModel(@NonNull Application application) {
        super(application);
        this.repo = new DeckInfoRepo(application);

    }

    /**
     * Sets current deck and its cards in the model
     * @param deckId
     * @return current Deck
     */
    public LiveData<Deck> getDeck (String deckId) {
        currentDeck = repo.getDeck(deckId);
        cards = repo.getAllCards(deckId);
        return currentDeck;
    }

    public void createCard() {
        String deckId = currentDeck.getValue().getUuid();
        CreateCardTask task = new CreateCardTask();
        task.execute(deckId);
    }

    private class CreateCardTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Card newCard = new Card(strings[0]);
            String isNull = String.valueOf((newCard == null));
            Log.d(isNull, "New Card is Null: ");
            repo.createCard(newCard);
            return null;
        }
    }


    // DAO independant methods

    public LiveData<Deck> getCurrentDeck() {
        return currentDeck;
    }

    public void setCurrentDeck(LiveData<Deck> currentDeck) {
        this.currentDeck = currentDeck;
    }

    public LiveData<List<Card>> getCards() {
        return cards;
    }

    public void setCards(LiveData<List<Card>> cards) {
        this.cards = cards;
    }


}
