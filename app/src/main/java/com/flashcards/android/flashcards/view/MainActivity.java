package com.flashcards.android.flashcards.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.MainModel;
import com.flashcards.android.flashcards.lib.JsonParser;
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

        recyclerView = (RecyclerView) findViewById(R.id.rec_main_activity);
        addButton = (FloatingActionButton) findViewById(R.id.fab_add_main);
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
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeckAdapter(decks, mainModel, this, this);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDeck();
            }
        });

        final Intent intent = getIntent();
        final String action = intent.getAction();

        if(Intent.ACTION_VIEW.equals(action)) {
            Uri filePath = intent.getData();
            importDeck(filePath);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_multiplayer) {
            Toast.makeText(getApplicationContext(),"Coming Soon...",Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(),"Settings Selected",Toast.LENGTH_LONG).show();
            return true;
        } else return super.onOptionsItemSelected(item);

    }

    private void importDeck(Uri uri) {
        try {

            InputStream is = getContentResolver().openInputStream(uri);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            SimpleDeck deck = JsonParser.readJson(json);

            Deck newDeck = new Deck(deck.getName());

            mainModel.insertDeck(newDeck);
            mainModel.insertSimpleCards(newDeck, deck.getCards());

            Toast.makeText(this, "Deck imported", Toast.LENGTH_LONG).show();

        } catch (IOException ex) {
            Toast.makeText(this, "File not found", Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
                Deck newDeck = new Deck(deckName[0]);
                mainModel.insertDeck(newDeck);

                // TODO: this should show up as a confirmation from Room db
                String alert = "Deck " + deckName[0] + " created";
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


    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }
}
