package com.flashcards.android.flashcards.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.EditModel;
import com.flashcards.android.flashcards.lib.Card;
import com.flashcards.android.flashcards.lib.Deck;

import jp.wasabeef.richeditor.RichEditor;

import static com.flashcards.android.flashcards.data.MockDeck.getFakeDeck;

public class CardEditActivity extends AppCompatActivity {
    RichEditor editor;
    TextView titleText;

    Button boldButton;
    Button italicButton;
    Button underlineButton;
    Button blackButton;
    Button blueButton;
    Button greenButton;
    Button undoButton;
    Button redoButton;
    Button flipButton;

    Deck deck;
    Card currentCard;
    boolean qSide;      // Indicates which side is being viewed (true = question, false = answer)

    EditModel model;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                if (currentCard == null) {
                    currentCard = card;
                    editor.setPlaceholder("Please Enter Question Here");
                    if (currentCard.getQuestion() != null) {
                        editor.setHtml(currentCard.getQuestion());
                    } else {
                        editor.setHtml("");
                    }
                    titleText.setText("Question");
                    qSide = true;
                } else {
                    currentCard = card;
                }
            }
        });


        // TODO: save changes to edited card
        editor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                if (qSide) {
                    model.updateQuestion(text);
                } else if (!qSide) {
                    model.updateAnswer(text);
                }
            }
        });
    }

    private void initFlipButton() {
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qSide) {
                    editor.setPlaceholder("Please Enter Answer Here");
                    if (currentCard.getAnswer() != null) {
                        editor.setHtml(currentCard.getAnswer());
                    } else {
                        editor.setHtml("");

                    }

                    titleText.setText("Answer");
                    qSide = false;

                } else if (!qSide) {
                    editor.setPlaceholder("Please Enter Question Here");
                    if (currentCard.getQuestion() != null) {
                        editor.setHtml(currentCard.getQuestion());
                    } else {
                        editor.setHtml("");
                    }
                    titleText.setText("Question");
                    qSide = true;
                }
            }
        });
    }


    private void initLayout() {
        titleText = (TextView) findViewById(R.id.tv_edit_title);

        boldButton = (Button) findViewById(R.id.question_btn_bold);
        italicButton = (Button) findViewById(R.id.question_btn_italic);
        underlineButton = (Button) findViewById(R.id.question_btn_underline);
        blackButton = (Button) findViewById(R.id.question_btn_color_black);
        blueButton = (Button) findViewById(R.id.question_btn_color_blue);
        greenButton = (Button) findViewById(R.id.question_btn_color_green);
        undoButton = (Button) findViewById(R.id.question_btn_undo);
        redoButton = (Button) findViewById(R.id.question_btn_redo);
        flipButton = (Button) findViewById(R.id.btn_edit_flip);
    }

    private void initEditor() {
        //TODO: Add justify, strikethrough, highlight buttons

        editor = (RichEditor) findViewById(R.id.question_editor_module);
        editor.setPadding(20,20,20,20);
        editor.setBackgroundColor(getResources().getColor(R.color.cardBackground));
        editor.setEditorFontSize(32);
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
    }

}
