package com.flashcards.android.flashcards.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.flashcards.android.flashcards.data.MainRepo;
import com.flashcards.android.flashcards.lib.Deck;

import java.util.List;

/**
 * Created by Abdullah Ali on 15/07/2018
 */
public class MainModel extends AndroidViewModel{
    private MainRepo repo;
    private LiveData<List<Deck>> decks;

    public MainModel(Application application) {
        super(application);
        this.repo = new MainRepo(application);
        decks = repo.getAllDecks();
    }

    public LiveData<List<Deck>> getAllDecks() {
        return decks;
    }


    public void insertDeck(Deck deck) {
        InsertDeckTask task = new InsertDeckTask();
        task.execute(deck);

    }
    private class InsertDeckTask extends AsyncTask<Deck, Void, Void> {
        @Override
        protected Void doInBackground(Deck... decks) {
            repo.addDeck(decks[0]);
            return null;
        }
    }

    public void deleteDeck(Deck deck) {
        DeleteDeckTask task = new DeleteDeckTask();
        task.execute(deck);

    }
    private class DeleteDeckTask extends AsyncTask<Deck, Void, Void> {
        @Override
        protected Void doInBackground(Deck... decks) {
            repo.deleteDeck(decks[0]);
            return null;
        }
    }


}
