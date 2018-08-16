package com.flashcards.android.flashcards.view;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.MainModel;
import com.flashcards.android.flashcards.lib.misc.JsonParser;
import com.flashcards.android.flashcards.lib.misc.NotificationService;
import com.flashcards.android.flashcards.lib.model.Deck;
import com.flashcards.android.flashcards.lib.model.SimpleDeck;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Abdullah Ali
 *
 * This is the view class of the Home page, which displays the decks created by the user.
 */

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DeckAdapter adapter;
    private List<Deck> decks;
    private MainModel mainModel;
    private FloatingActionButton addButton;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rec_main_activity);
        addButton = findViewById(R.id.fab_add_main);
        context = this;

        // Link View with ViewModel
        mainModel = ViewModelProviders.of(this).get(MainModel.class);

        // Add observer for changes to decks
        mainModel.getAllDecks().observe(this, new Observer<List<Deck>>() {
            @Override
            public void onChanged(@Nullable final List<Deck> decks) {
                setDecks(decks);
                adapter.setDecks(decks);
                adapter.notifyDataSetChanged();
                mainModel.setDecks(decks);
                checkForImports();
            }
        });

        // Setup recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeckAdapter(decks, mainModel, this, this);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDeck();
            }
        });

        scheduleNotifications();

    }

    /**
     * Import deck method
     * Takes an address of a JSon file of extension .deck
     * Converts the simpleDeck and simpleCard
     */
    private void checkForImports() {
        final Intent intent = getIntent();
        final String action = intent.getAction();

        if(Intent.ACTION_VIEW.equals(action) && !mainModel.deckImported()) {
            Uri uri = intent.getData();
            try {
                InputStream is = getContentResolver().openInputStream(uri);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String json = new String(buffer, "UTF-8");
                SimpleDeck deck = JsonParser.readJson(json);

                String name = mainModel.validateName(deck.getName());
                Deck newDeck = new Deck(name);

                mainModel.insertDeck(newDeck);
                mainModel.insertSimpleCards(newDeck, deck.getCards());
                Toast.makeText(this, "Deck imported", Toast.LENGTH_SHORT).show();

            } catch (IOException ex) {
                Toast.makeText(this, "File not found", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(this, "File could not be imported", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            mainModel.setDeckImported(true);

        }

    }


    private void addDeck() {
        final String[] deckName = new String[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create a new Deck");

        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Please enter the deck name");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        // Set up the buttons
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deckName[0] = input.getText().toString();

                String validName = mainModel.validateName(deckName[0]);
                Deck newDeck = new Deck(validName);
                mainModel.insertDeck(newDeck);

                // TODO: this should show up as a confirmation from Room db
                String alert = validName + " deck created";
                Toast.makeText(context, alert, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

    /**
     * This method uses jobScheduler to schedule notifications 24 hours after the app is used,
     * if there are outstanding decks of cards that need to be revised.
     *
     * The notification interval is set to 24 hours, so that notifications appear roughly at the time,
     * the user is most likely to use the app.
     *
     * If the user uses the app within 24 hours, the timer resets
     * (ie: the notifications only appear if it has been longer then 24 hours since last app use)
     */
    public void scheduleNotifications () {
        // Sets the delay (in ms) between notifications.
        long delay = (24*60*60*1000);      // 24 Hour in ms.

        ComponentName componentName = new ComponentName(this, NotificationService.class);
        JobInfo info = new JobInfo.Builder(2121, componentName)
                .setMinimumLatency(delay)
                .setPersisted(true)
                .setBackoffCriteria(delay, JobInfo.BACKOFF_POLICY_LINEAR)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
    }

    /**
     * This method can be called if the notifications job needs to be cancelled.
     */
    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(2121);
    }

    // Redraws the whole view every time it is requested (As opposed to pausing it in the background)
    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

}
