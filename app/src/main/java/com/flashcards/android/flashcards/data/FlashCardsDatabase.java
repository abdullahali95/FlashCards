package com.flashcards.android.flashcards.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.CardsDAO;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.lib.DeckDAO;
import com.flashcards.android.flashcards.lib.Progress;
import com.flashcards.android.flashcards.lib.ProgressDAO;

/**
 * Created by Abdullah Ali on 15/07/2018
 */

@Database(entities = {Card.class, Deck.class, Progress.class}, version = 1, exportSchema = false)
public abstract class FlashCardsDatabase extends RoomDatabase {

    public abstract CardsDAO cardsDAO();
    public abstract DeckDAO deckDAO();
    public abstract ProgressDAO progressDAO();

    // Database lives here
    private static FlashCardsDatabase flashCardsDB;

    public static FlashCardsDatabase getFlashCardsDB(final Context context) {
        if (flashCardsDB == null) {
            synchronized (FlashCardsDatabase.class) {
                if (flashCardsDB == null) {
                    flashCardsDB = Room.databaseBuilder(context.getApplicationContext(),
                            FlashCardsDatabase.class, "flashcards_database")
                            .fallbackToDestructiveMigration()
//                            .addCallback(sRoomDatabaseCallback) // temp method for adding test data
                            .build();
                }
            }
        }
        return flashCardsDB;
    }

    // Adding test data to database
    //TODO: delete this once working

//    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
//
//        @Override
//        public void onOpen (@NonNull SupportSQLiteDatabase db){
//            super.onOpen(db);
//            // If you want to keep the data through app restarts,
//            // comment out the following line.
//             new PopulateDbAsync(flashCardsDB).execute();
//        }
//    };
//
//    /**
//     * Populate the database in the background.
//     * If you want to start with more words, just add them.
//     */
//    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
//
//        private final CardsDAO cardsDAO;
//
//        PopulateDbAsync(FlashCardsDatabase db) {
//            cardsDAO = db.cardsDAO();
//        }
//
//        @Override
//        protected Void doInBackground(final Void... params) {
//            // Start the app with a clean database every time.
//            // Not needed if you only populate on creation.
//            // deckDAO.deleteAll();
//
//            Card card = new Card("717374d8-4be7-47c1-8890-ba3940838105");
//            cardsDAO.createCard(card);
//            return null;
//        }
//    }



}
