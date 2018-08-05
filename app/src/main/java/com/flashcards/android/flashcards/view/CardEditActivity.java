package com.flashcards.android.flashcards.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.EditModel;
import com.flashcards.android.flashcards.lib.model.Card;
import com.flashcards.android.flashcards.lib.model.Deck;

import jp.wasabeef.richeditor.RichEditor;

public class CardEditActivity extends AppCompatActivity {
    RichEditor editor;
    TextView titleText;

    ImageButton boldButton;
    ImageButton italicButton;
    ImageButton underlineButton;
    Button blackButton;
    Button blueButton;
    Button greenButton;
    ImageButton undoButton;
    ImageButton redoButton;
    ImageButton ulButton;
    FloatingActionButton flipButton;
    ImageButton strikeThroughButton;

    EditModel model;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_edit_card);

        Bundle bundle = getIntent().getExtras();
        int cardId = bundle.getInt("cardId");
        String deckId = bundle.getString("deckId");
        Log.d(String.valueOf(cardId), "CardEditActivity: ");
        Log.d(deckId, "CardEditActivity: ");

        model = ViewModelProviders.of(this).get(EditModel.class);

        initLayout();
        initEditor();
        initToolbar();
        initFlipButton();

        model.getCard(cardId, deckId).observe(this, new Observer<Card>() {
            @Override
            public void onChanged(@Nullable Card card) {
                if (model.getCurrentCard() == null) {
                    model.setCurrentCard(card);
                    editor.setPlaceholder("Please Enter Question Here");
                    if (model.getCurrentCard().getQuestion() != null) {
                        editor.setHtml(model.getCurrentCard().getQuestion());
                    } else {
                        editor.setHtml("");
                    }
                    titleText.setText("Question");
                    model.setQside(true);
                } else {
                    model.setCurrentCard(card);
                }
            }
        });


        // TODO: save changes to edited card
        editor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                if (model.isQside()) {
                    model.updateQuestion(text);
                } else {
                    model.updateAnswer(text);
                }
            }
        });
    }

    /**
     * This method is overridden to handle orientation changes gracefully
     */
    @Override
    public void onResume() {
        super.onResume();
        if (model.isQside()) {
            // Question side was active
            if (model.getCurrentCard() != null && model.getCurrentCard().getQuestion() != null) {
                editor.setHtml(model.getCurrentCard().getQuestion());
            } else {
                editor.setHtml("");
            }
            titleText.setText("Question");
        } else {
            // Answer side was active
            if (model.getCurrentCard() != null && model.getCurrentCard().getAnswer() != null) {
                editor.setHtml(model.getCurrentCard().getAnswer());
            } else {
                editor.setHtml("");
            }
            titleText.setText("Answer");
        }
    }

    private void initFlipButton() {
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.isQside()) {
                    editor.setPlaceholder("Please Enter Answer Here");
                    if (model.getCurrentCard().getAnswer() != null) {
                        editor.setHtml(model.getCurrentCard().getAnswer());
                    } else {
                        editor.setHtml("");

                    }

                    titleText.setText("Answer");
                    model.setQside(false);

                } else if (!model.isQside()) {
                    editor.setPlaceholder("Please Enter Question Here");
                    if (model.getCurrentCard().getQuestion() != null) {
                        editor.setHtml(model.getCurrentCard().getQuestion());
                    } else {
                        editor.setHtml("");
                    }
                    titleText.setText("Question");
                    model.setQside(true);
                }
            }
        });
    }


    private void initLayout() {
        titleText = (TextView) findViewById(R.id.tv_edit_title);

        boldButton = (ImageButton) findViewById(R.id.question_btn_bold);
        italicButton = (ImageButton) findViewById(R.id.question_btn_italic);
        underlineButton = (ImageButton) findViewById(R.id.question_btn_underline);
        blackButton = (Button) findViewById(R.id.question_btn_color_black);
        blueButton = (Button) findViewById(R.id.question_btn_color_blue);
        greenButton = (Button) findViewById(R.id.question_btn_color_green);
        undoButton = (ImageButton) findViewById(R.id.question_btn_undo);
        redoButton = (ImageButton) findViewById(R.id.question_btn_redo);
        flipButton = (FloatingActionButton) findViewById(R.id.btn_edit_flip);
        ulButton = (ImageButton) findViewById(R.id.question_btn_unordered_list);
        strikeThroughButton = (ImageButton) findViewById(R.id.question_btn_strikethrough);
    }

    private void initEditor() {
        //TODO: Add justify, strikethrough, highlight buttons

        editor = (RichEditor) findViewById(R.id.question_editor);
        editor.setPadding(20,20,20,20);
        editor.setBackgroundColor(getResources().getColor(R.color.cardBackground));
        editor.setEditorFontSize(24);
    }


    private void initToolbar() {
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

        ulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setBullets();
            }
        });

        strikeThroughButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setStrikeThrough();
            }
        });
    }

}
