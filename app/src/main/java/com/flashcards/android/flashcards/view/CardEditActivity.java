package com.flashcards.android.flashcards.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.EditModel;
import com.flashcards.android.flashcards.lib.model.Card;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by Abdullah Ali
 *
 * This is the view class for the Edit Cards Activity
 */
public class CardEditActivity extends AppCompatActivity implements View.OnTouchListener {
    private RichEditor editor;
    private TabLayout tabs;

    // Buttons for Editing toolbar
    private ImageButton boldButton, italicButton, underlineButton, redButton, blueButton,
            greenButton, undoButton, redoButton, ulButton, strikeThroughButton,
            blueHighlightButton, greenHighlightButton, clearFormatButton, redHighlightButton;

    private Button addCardButton;
    private EditModel model;
    private GestureDetectorCompat gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        Bundle bundle = getIntent().getExtras();
        int cardId = bundle.getInt("cardId");
        String deckId = bundle.getString("deckId");

        // Set view model
        model = ViewModelProviders.of(this).get(EditModel.class);

        // Initialise view
        initLayout();
        initEditor();
        initToolbar();
        initTabButtons();
        initAddCardButton();

        // Observe for changes to card
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
                    model.setTabSelected(0);
                } else {
                    model.setCurrentCard(card);
                }
            }
        });

        // Save changes
        editor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                if (model.getTabSelected() == 0) {
                    model.updateQuestion(text);
                } else if (model.getTabSelected() == 1){
                    model.updateAnswer(text);
                } else {
                    model.updateHint(text);
                }
            }
        });

        // Add gestures support for switching between questions and answers
        gestureDetector = new GestureDetectorCompat(this, new MyGestureListener());
        editor.setOnTouchListener(this);

    }

    /**
     * This method is overridden to handle orientation changes gracefully
     */
    @Override
    public void onResume() {
        super.onResume();
        if (model.getCurrentCard() != null) {
            if (model.getTabSelected() == 0) {
                // Question side was active
                if (model.getCurrentCard() != null && model.getCurrentCard().getQuestion() != null) {
                    editor.setHtml(model.getCurrentCard().getQuestion());
                } else {
                    editor.setHtml("");
                }
                tabs.getTabAt(0).select();
            } else if (model.getTabSelected() == 1){
                // Answer side was active
                if (model.getCurrentCard() != null && model.getCurrentCard().getAnswer() != null) {
                    editor.setHtml(model.getCurrentCard().getAnswer());
                } else {
                    editor.setHtml("");
                }
                tabs.getTabAt(1).select();
            } else {
                // Hint side was active
                if (model.getCurrentCard() != null && model.getCurrentCard().getHint() != null) {
                    editor.setHtml(model.getCurrentCard().getHint());
                } else {
                    editor.setHtml("");
                }
                tabs.getTabAt(2).select();
            }
        }
    }

    private void initTabButtons() {

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.select();

                if (tabs.getTabAt(0).isSelected()) {
                    editor.setPlaceholder("Please Enter Question Here");
                    if (model.getCurrentCard().getQuestion() != null) {
                        editor.setHtml(model.getCurrentCard().getQuestion());
                    } else {
                        editor.setHtml("");
                    }
                    model.setTabSelected(0);

                } else if (tabs.getTabAt(1).isSelected()) {
                    editor.setPlaceholder("Please Enter Answer Here");
                    if (model.getCurrentCard().getAnswer() != null) {
                        editor.setHtml(model.getCurrentCard().getAnswer());
                    } else {
                        editor.setHtml("");

                    }
                    model.setTabSelected(1);
                } else if (tabs.getTabAt(2).isSelected()) {
                    editor.setPlaceholder("Please Enter Hint here (Optional)");
                    if (model.getCurrentCard().getHint() != null) {
                        editor.setHtml(model.getCurrentCard().getHint());
                    } else {
                        editor.setHtml("");

                    }
                    model.setTabSelected(2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab){}
        });

    }


    private void initLayout() {
        tabs = findViewById(R.id.edit_card_tabs);
        addCardButton = findViewById(R.id.btn_add_card);
        boldButton = findViewById(R.id.question_btn_bold);
        italicButton = findViewById(R.id.question_btn_italic);
        underlineButton = findViewById(R.id.question_btn_underline);
        redButton = findViewById(R.id.question_btn_color_red);
        blueButton = findViewById(R.id.question_btn_color_blue);
        greenButton = findViewById(R.id.question_btn_color_green);
        undoButton = findViewById(R.id.question_btn_undo);
        redoButton = findViewById(R.id.question_btn_redo);
        ulButton = findViewById(R.id.question_btn_unordered_list);
        strikeThroughButton = findViewById(R.id.question_btn_strikethrough);
        redHighlightButton = findViewById(R.id.question_btn_highlight_red);
        greenHighlightButton = findViewById(R.id.question_btn_highlight_green);
        blueHighlightButton = findViewById(R.id.question_btn_highlight_blue);
        clearFormatButton = findViewById(R.id.question_btn_clear_formatting);

    }

    private void initEditor() {
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

    public void initAddCardButton () {
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.createCard();
                Card newCard = model.lastEmptyCard();

                if (newCard != null) {
                    //Switch view
                    Intent intent = new Intent(CardEditActivity.this, CardEditActivity.class);
                    intent.putExtra("cardId", newCard.getCardId());
                    intent.putExtra("deckId", newCard.getDeckId());
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float dist = e2.getX() - e1.getX();
            if (velocityX < -4000 && dist < -100 && tabs.getTabAt(0).isSelected()) {
                editor.setPlaceholder("Please Enter Answer Here");
                if (model.getCurrentCard().getAnswer() != null) {
                    editor.setHtml(model.getCurrentCard().getAnswer());
                } else {
                    editor.setHtml("");
                }
                tabs.getTabAt(1).select();
                model.setTabSelected(1);
            } else if (velocityX < -4000 && dist < -100 && tabs.getTabAt(1).isSelected()) {
                editor.setPlaceholder("Please Enter Hint Here (Optional)");
                if (model.getCurrentCard().getHint() != null) {
                    editor.setHtml(model.getCurrentCard().getHint());
                } else {
                    editor.setHtml("");

                }
                tabs.getTabAt(2).select();
                model.setTabSelected(2);
            } else if (velocityX > 4000 && dist > 100 && tabs.getTabAt(1).isSelected()) {
                editor.setPlaceholder("Please Enter Question Here");
                if (model.getCurrentCard().getQuestion() != null) {
                    editor.setHtml(model.getCurrentCard().getQuestion());
                } else {
                    editor.setHtml("");
                }
                tabs.getTabAt(0).select();
                model.setTabSelected(0);
            } else if (velocityX > 4000 && dist > 100 && tabs.getTabAt(2).isSelected()) {
                editor.setPlaceholder("Please Enter Answer Here");
                if (model.getCurrentCard().getAnswer() != null) {
                    editor.setHtml(model.getCurrentCard().getAnswer());
                } else {
                    editor.setHtml("");

                }
                tabs.getTabAt(1).select();
                model.setTabSelected(1);
            }
            return true;
        }
    }


    // Clean up methods
    @Override
    public void onBackPressed() {
        model.deleteIfEmpty();
        finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
        model.deleteIfEmpty();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        model.deleteIfEmpty();
    }

}
