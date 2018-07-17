package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.flashcards.android.flashcards.data.DeckInfoRepo;
import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;

import java.util.List;

/**
 * Created by Abdullah Ali on 16/07/2018
 */
public class DeckInfoModel extends AndroidViewModel {
    private DeckInfoRepo repo;
    private Deck currentDeck;
    private LiveData<List<Card>> cards;

    public DeckInfoModel(@NonNull Application application) {
        super(application);
        this.repo = new DeckInfoRepo(application);
        cards = repo.getAllCards("d317109d-1f52-4bdb-86ef-112245e1fc95");
    }

    /**
     * Sets current deck and its cards in the model
     * @param deckId
     * @return current Deck
     */
    public LiveData<Deck> getDeck (String deckId) {
        currentDeck = repo.getDeck(deckId).getValue();
        cards = repo.getAllCards(deckId);
        return repo.getDeck(deckId);
    }

    public void createCard() {
        String deckId = currentDeck.getDeckId();
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

    public Deck getCurrentDeck() {
        return currentDeck;
    }

    public void setCurrentDeck(Deck currentDeck) {
        this.currentDeck = currentDeck;
    }

    public LiveData<List<Card>> getCards() {
        return cards;
    }

    public void setCards(LiveData<List<Card>> cards) {
        this.cards = cards;
    }


}
