package com.flashcards.android.flashcards.lib;

import android.arch.persistence.room.TypeConverter;

import com.google.common.collect.EvictingQueue;

import java.util.Date;

/**
 * Created by Abdullah Ali on 19/07/2018
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
        EvictingQueue<Boolean> b = EvictingQueue.create(10);
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
        String s = "";
        Boolean current;
        if (b == null) return s;
        for (int i = 0; i < b.size(); i++) {
            current = b.poll();
            if (current == Boolean.TRUE) {
                s += "t";
            } else if (current == Boolean.FALSE) {
                s += "f";;
            }
        }
        return s;
    }
}
