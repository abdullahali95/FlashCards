package com.flashcards.android.flashcards.lib.misc;

import android.arch.persistence.room.TypeConverter;

import com.google.common.collect.EvictingQueue;

import java.util.Date;

/**
 * Created by Abdullah Ali on 19/07/2018
 *
 * This is a data convertor class for storing complex objects in the SQLite database
 */
public class Converters {

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toLong(Date value) {
        return value == null ? null : value.getTime();
    }

    @TypeConverter
    public static EvictingQueue<Boolean> fromString (String s) {
        EvictingQueue<Boolean> b = EvictingQueue.create(5);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 't') {
                b.add(Boolean.TRUE);
            } else if (s.charAt(i) == 'f') {
                b.add(Boolean.FALSE);
            }
        }

        return b;
    }

    @TypeConverter
    public static String QueueToString (EvictingQueue<Boolean> b) {
        StringBuilder s = new StringBuilder();
        if (b == null) return s.toString();
        for (Boolean current : b) {
            if (current.equals(Boolean.TRUE)) {
                s.append("t");
            } else if (current.equals(Boolean.FALSE)) {
                s.append("f");
            }
        }
        return s.toString();
    }
}
