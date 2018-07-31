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
 */

@Dao
public interface DeckDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addDeck(Deck deck);

    @Delete
    public void deleteDeck(Deck deck);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void setDeck(Deck deck);

    @Query("UPDATE Deck SET deckId = :newUuid WHERE deckId =:oldUuid")
    public void setUuid(String oldUuid, String newUuid);

    @Query("SELECT * FROM Deck")
    public LiveData<List<Deck>> getAllDecks ();

//   @Query("SELECT * FROM Deck WHERE lastUsed NOT LIKE :deckId LIMIT 1")
   @Query("SELECT * FROM Deck WHERE deckId = :deckId")
    public LiveData<Deck> getDeck (String deckId);

    @Query("SELECT name FROM Deck WHERE deckId = :deckId")
    public LiveData<String> getName(String deckId);

    @Query("UPDATE Deck SET name = :name WHERE deckId =:deckId")
    public void setName(String deckId, String name);

    @Query("UPDATE Deck SET lastUsed = :lastUsed WHERE deckId =:deckId")
    public void setLastUsed(String deckId, String lastUsed);

    @Query("UPDATE Deck SET ef = :ef WHERE deckId =:deckId")
    public void setEf(String deckId, double ef);

    @Query("UPDATE Deck SET deckSize = :size WHERE deckId =:deckId")
    public void setDeckSize(String deckId, int size);

    @Query("SELECT deckSize FROM Deck WHERE deckId = :deckId")
    public LiveData<Integer> getDeckSize(String deckId);

}
