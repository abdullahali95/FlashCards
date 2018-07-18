package com.flashcards.android.flashcards.lib;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Query("SELECT name FROM Deck WHERE deckId = :uuid")
    public LiveData<String> getName(String uuid);

    @Query("UPDATE Deck SET name = :name WHERE deckId =:uuid")
    public void setName(String uuid, String name);

    @Query("UPDATE Deck SET lastUsed = :lastUsed WHERE deckId =:uuid")
    public void setLastUsed(String uuid, String lastUsed);


}
