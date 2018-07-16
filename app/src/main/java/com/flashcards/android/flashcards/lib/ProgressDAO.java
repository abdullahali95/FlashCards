package com.flashcards.android.flashcards.lib;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.flashcards.android.flashcards.lib.Progress;
import com.google.common.collect.EvictingQueue;

import java.util.List;

/**
 * Created by abdul on 15/07/2018
 */

@Dao
public interface ProgressDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addProgress(Progress progress);

    @Delete
    public void deleteProgress (Progress progress);

    @Query("SELECT * FROM 'progress' WHERE 'deckId' =:deckId ")
    LiveData<List<Progress>> getAllProgress(String deckId);

    @Query("SELECT * FROM 'Progress' WHERE 'cardId' = :cardId AND 'deckId' = :deckId")
    public LiveData<Progress> getProgress(int cardId, String deckId);

    @Query("UPDATE Card SET deckId = :newDeckId WHERE 'cardId' =:cardId AND 'deckId' = :oldDeckId")
    public void setDeckId(int cardId, String oldDeckId, String newDeckId);

    @Query("UPDATE Progress SET attempts = :attempts WHERE 'cardId' =:cardId AND 'deckId' = :deckId")
    public void setAttempts(int cardId, String deckId, int attempts);

    @Query("UPDATE Progress SET attempts = attempts + 1 WHERE 'cardId' =:cardId AND 'deckId' = :deckId")
    public void incAttempts(int cardId, String deckId);

    @Query("UPDATE Progress SET correct = :correct WHERE 'cardId' =:cardId AND 'deckId' = :deckId")
    public void setCorrect(int cardId, String deckId, int correct);

    @Query("UPDATE Progress SET correct = correct + 1 WHERE 'cardId' =:cardId AND 'deckId' = :deckId")
    public void incCorrect(int cardId, String deckId);

    //TODO: fix the storage of these
//    @Query("UPDATE Progress SET lastTen = :lastTen WHERE 'cardId' =:cardId AND 'deckId' = :deckId")
//    public void setLastTen(int cardId, String deckId, EvictingQueue<Boolean> lastTen);

}
