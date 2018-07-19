package com.flashcards.android.flashcards.data;

import android.arch.persistence.room.TypeConverter;

import com.google.common.collect.EvictingQueue;

/**
 * Created by Abdullah Ali on 19/07/2018
 */
public class Converters {

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
