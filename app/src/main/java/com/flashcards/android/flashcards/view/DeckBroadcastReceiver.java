package com.flashcards.android.flashcards.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Abdullah Ali on 23/07/2018
 */
public class DeckBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
// TODO Auto-generated method stub
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("URI", intent.getData());
        Log.d("onReceive: ", intent.getData().toString());
        context.startActivity(i);
    }
}
