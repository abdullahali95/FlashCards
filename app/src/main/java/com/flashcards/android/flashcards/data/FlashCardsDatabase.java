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
 *
 * This is the database class, which is used to store an SQLite database using the Room library
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


}
