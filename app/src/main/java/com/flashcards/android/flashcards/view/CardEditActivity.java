package com.flashcards.android.flashcards.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.EditModel;
import com.flashcards.android.flashcards.lib.model.Card;

import jp.wasabeef.richeditor.RichEditor;

public class CardEditActivity extends AppCompatActivity {
    private RichEditor editor;
    private TextView titleText;

    // Buttons for Editing toolbar
    private ImageButton boldButton, italicButton, underlineButton, redButton, blueButton,
            greenButton, undoButton, redoButton, ulButton, strikeThroughButton,
            blueHighlightButton, greenHighlightButton, clearFormatButton, redHighlightButton;

    private FloatingActionButton flipButton;

    private EditModel model;


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
        titleText = findViewById(R.id.tv_edit_title);

        boldButton = findViewById(R.id.question_btn_bold);
        italicButton = findViewById(R.id.question_btn_italic);
        underlineButton = findViewById(R.id.question_btn_underline);
        redButton = findViewById(R.id.question_btn_color_red);
        blueButton = findViewById(R.id.question_btn_color_blue);
        greenButton = findViewById(R.id.question_btn_color_green);
        undoButton = findViewById(R.id.question_btn_undo);
        redoButton = findViewById(R.id.question_btn_redo);
        flipButton = findViewById(R.id.btn_edit_flip);
        ulButton = findViewById(R.id.question_btn_unordered_list);
        strikeThroughButton = findViewById(R.id.question_btn_strikethrough);
        redHighlightButton = findViewById(R.id.question_btn_highlight_red);
        greenHighlightButton = findViewById(R.id.question_btn_highlight_green);
        blueHighlightButton = findViewById(R.id.question_btn_highlight_blue);
        clearFormatButton = findViewById(R.id.question_btn_clear_formatting);

    }

    private void initEditor() {
        //TODO: Add justify, strikethrough, highlight buttons

        editor = findViewById(R.id.question_editor);
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

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setTextColor(getResources().getColor(R.color.red));
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

        redHighlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setTextBackgroundColor(getResources().getColor(R.color.red_highlight));
            }
        });

        blueHighlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setTextBackgroundColor(getResources().getColor(R.color.blue_highlight));
            }
        });

        greenHighlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.setTextBackgroundColor(getResources().getColor(R.color.green_highlight));
            }
        });

        clearFormatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.removeFormat();
            }
        });
    }

}
