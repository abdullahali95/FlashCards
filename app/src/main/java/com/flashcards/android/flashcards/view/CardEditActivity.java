package com.flashcards.android.flashcards.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;

import jp.wasabeef.richeditor.RichEditor;

import static com.flashcards.android.flashcards.repo.MockDeck.getFakeDeck;

public class CardEditActivity extends AppCompatActivity {
    RichEditor editor;
    Button boldButton;
    Button italicButton;
    Button underlineButton;
    Button blackButton;
    Button blueButton;
    Button greenButton;
    Button undoButton;
    Button redoButton;

    Deck deck;
    Card currentCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

//        Bundle bundle = getIntent().getExtras();
//        String deckId = bundle.getParcelable("Deck");


        initialiseEditor();
        initialiseLayout();
        initialiseToolbar();

        // Load card
        deck = getFakeDeck();
        currentCard = deck.getCards().get(0);
        editor.setHtml(currentCard.getQuestion());

        Log.d("editor", editor.getHtml());

        // TODO: save changes to edited card
        editor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {

            }
        });
    }



    private void initialiseLayout() {
        boldButton = (Button) findViewById(R.id.question_btn_bold);
        italicButton = (Button) findViewById(R.id.question_btn_italic);
        underlineButton = (Button) findViewById(R.id.question_btn_underline);
        blackButton = (Button) findViewById(R.id.question_btn_color_black);
        blueButton = (Button) findViewById(R.id.question_btn_color_blue);
        greenButton = (Button) findViewById(R.id.question_btn_color_green);
        undoButton = (Button) findViewById(R.id.question_btn_undo);
        redoButton = (Button) findViewById(R.id.question_btn_redo);
    }

    private void initialiseEditor() {
        //TODO: Add justify, strikethrough, highlight buttons

        editor = (RichEditor) findViewById(R.id.question_editor_module);
        editor.setPadding(20,20,20,20);
        editor.setPlaceholder("Please Enter Question Here");
        editor.setBackgroundColor(getResources().getColor(R.color.cardBackground));
        editor.setEditorFontSize(32);
    }


    private void initialiseToolbar() {
        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setBold();
            }
        });

        italicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setItalic();
            }
        });

        underlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setUnderline();
            }
        });

        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setTextColor(Color.BLACK);
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.undo();
            }
        });

        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.redo();
            }
        });

        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setTextColor(getResources().getColor(R.color.blue));
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setTextColor(getResources().getColor(R.color.green));
            }
        });
    }

}
