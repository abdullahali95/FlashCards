package com.flashcards.android.flashcards.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.flashcards.android.flashcards.lib.DAO.CardsDAO;
import com.flashcards.android.flashcards.lib.DAO.DeckDAO;
import com.flashcards.android.flashcards.lib.misc.Converters;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;

/**
 * Created by Abdullah Ali on 15/07/2018
 */

@Database(entities = {Card.class, Deck.class}, version = 9, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class FlashCardsDatabase extends RoomDatabase {

    public abstract CardsDAO cardsDAO();
    public abstract DeckDAO deckDAO();

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

    /**
     * Methdd for when Application not required.
     * @return FlashCardsDB
     */
    public static FlashCardsDatabase getFlashCardsDB() {

        return flashCardsDB;
    }


    // Adding test data to database
    //TODO: delete this once working
//
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
//            Card card = new Card("0d2a51db-9b01-4f76-93e5-2621c451e158");
//            cardsDAO.createCard(card);
//            return null;
//        }
//    }



}
