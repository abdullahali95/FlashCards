package com.flashcards.android.flashcards.lib.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.flashcards.android.flashcards.lib.model.Deck;

import java.util.List;

/**
 * Created by Abdullah Ali on 14/07/2018
 *
 * This is the Data Access Object interface for the Deck entity
 */

@Dao
public interface DeckDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addDeck(Deck deck);

    @Delete
    void deleteDeck(Deck deck);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void setDeck(Deck deck);

    @Query("UPDATE Deck SET deckId = :newUuid WHERE deckId =:oldUuid")
    void setUuid(String oldUuid, String newUuid);

    @Query("SELECT * FROM Deck")
    LiveData<List<Deck>> getAllDecks();

    @Query("SELECT * FROM Deck WHERE deckId = :deckId")
    LiveData<Deck> getDeck(String deckId);

    @Query("SELECT * FROM Deck WHERE deckId = :deckId")
    Deck getDeckNow(String deckId);

    @Query("SELECT name FROM Deck WHERE deckId = :deckId")
    LiveData<String> getName(String deckId);

    @Query("UPDATE Deck SET name = :name WHERE deckId =:deckId")
    void setName(String deckId, String name);

    @Query("UPDATE Deck SET lastUsed = :lastUsed WHERE deckId =:deckId")
    void setLastUsed(String deckId, String lastUsed);

    @Query("UPDATE Deck SET ef = :ef WHERE deckId =:deckId")
    void setEf(String deckId, double ef);

    @Query("UPDATE Deck SET deckSize = :size WHERE deckId =:deckId")
    void setDeckSize(String deckId, int size);

    @Query("SELECT deckSize FROM Deck WHERE deckId = :deckId")
    LiveData<Integer> getDeckSize(String deckId);

    //TODO: test if the ls condition is working
    @Query("SELECT nextTestDue FROM Deck WHERE ls > 2")
    List<String> getAllNextDue();

}
