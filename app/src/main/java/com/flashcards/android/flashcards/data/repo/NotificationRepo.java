package com.flashcards.android.flashcards.data.repo;

import android.app.Application;

import com.flashcards.android.flashcards.data.FlashCardsDatabase;
import com.flashcards.android.flashcards.lib.DAO.DeckDAO;

import java.util.List;

/**
 * Created by Abdullah Ali on 04/08/2018
 */
public class NotificationRepo {
    private final DeckDAO deckDAO;


    public NotificationRepo(Application application) {
        FlashCardsDatabase db = FlashCardsDatabase.getFlashCardsDB(application);
        this.deckDAO = db.deckDAO();
    }

    public List<String> getAllNextDue () {
        return deckDAO.getAllNextDue();
    }

}
