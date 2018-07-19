package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.flashcards.android.flashcards.data.repo.DeckInfoRepo;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;

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
    }

    /**
     * Sets current deck and its cards in the model
     * @param deckId
     * @return current Deck
     */
    public LiveData<Deck> getDeck (String deckId) {
        currentDeck = repo.getDeck(deckId).getValue();
        getAllCards(deckId);
        return repo.getDeck(deckId);
    }

    public LiveData<List<Card>> getAllCards (String deckId) {
        cards = repo.getAllCards(deckId);
        return cards;
    }

    public void createCard() {
        String deckId = currentDeck.getDeckId();
        CreateCardTask task = new CreateCardTask();
        task.execute(deckId);
    }

    private class CreateCardTask extends AsyncTask<String, Void, Long> {

        @Override
        protected Long doInBackground(String... strings) {
            Card newCard = new Card(strings[0]);

            return repo.createCard(newCard);
        }
    }


    public void deleteCard(Card card) {
        DeleteCardTask task = new DeleteCardTask();
        task.execute(card);
    }

    private class DeleteCardTask extends AsyncTask<Card, Void, Integer> {

        @Override
        protected Integer doInBackground(Card... cards) {
            Card card = cards[0];
            return repo.deleteCard(card);
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
