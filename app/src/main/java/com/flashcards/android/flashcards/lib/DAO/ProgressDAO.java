package com.flashcards.android.flashcards.lib.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.flashcards.android.flashcards.lib.model.Progress;
import com.google.common.collect.EvictingQueue;

import java.util.List;

/**
 * Created by abdul on 15/07/2018
 */

@Dao
public interface ProgressDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addProgress(Progress progress);

    @Delete
    void deleteProgress(Progress progress);

    @Query("SELECT * FROM Progress WHERE deckId =:deckId ")
    LiveData<List<Progress>> getAllProgress(String deckId);

    @Query("SELECT * FROM Progress WHERE cardId = :cardId AND deckId = :deckId")
    LiveData<Progress> getProgress(int cardId, String deckId);

    @Query("UPDATE Card SET deckId = :newDeckId WHERE cardId =:cardId AND deckId = :oldDeckId")
    void setDeckId(int cardId, String oldDeckId, String newDeckId);

    @Query("UPDATE Progress SET attempts = :attempts WHERE cardId =:cardId AND deckId = :deckId")
    void setAttempts(int cardId, String deckId, int attempts);

    @Query("UPDATE Progress SET attempts = attempts + 1 WHERE cardId =:cardId AND deckId = :deckId")
    void incAttempts(int cardId, String deckId);

    @Query("UPDATE Progress SET correct = :correct WHERE cardId =:cardId AND deckId = :deckId")
    void setCorrect(int cardId, String deckId, int correct);

    @Query("UPDATE Progress SET correct = correct + 1 WHERE cardId =:cardId AND deckId = :deckId")
    void incCorrect(int cardId, String deckId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void setProgress(Progress progress);

    //TODO: fix the storage of these
    @Query("UPDATE Progress SET lastTen = :lastTen WHERE cardId =:cardId AND deckId = :deckId")
    void setLastTen(int cardId, String deckId, EvictingQueue<Boolean> lastTen);

}
