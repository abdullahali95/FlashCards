package com.flashcards.android.flashcards.view;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.flashcards.android.flashcards.R;

import jp.wasabeef.richeditor.RichEditor;

public class AnswerEditActivity extends AppCompatActivity {

    RichEditor editor;
    Button boldButton;
    Button italicButton;
    Button underlineButton;
    Button blackButton;
    Button blueButton;
    Button greenButton;
    Button undoButton;
    Button redoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_edit);


        //TODO: Add justify, strikethrough, highlight buttons

        editor = (RichEditor) findViewById(R.id.answer_editor_module);
        editor.setPadding(20,20,20,20);
        editor.setPlaceholder("Please Enter Answer Here");
        editor.setBackgroundColor(getResources().getColor(R.color.cardBackground));
        editor.setEditorFontSize(24);

        boldButton = (Button) findViewById(R.id.answer_btn_bold);
        italicButton = (Button) findViewById(R.id.answer_btn_italic);
        underlineButton = (Button) findViewById(R.id.answer_btn_underline);
        blackButton = (Button) findViewById(R.id.answer_btn_color_black);
        blueButton = (Button) findViewById(R.id.answer_btn_color_blue);
        greenButton = (Button) findViewById(R.id.answer_btn_color_green);
        undoButton = (Button) findViewById(R.id.answer_btn_undo);
        redoButton = (Button) findViewById(R.id.answer_btn_redo);

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
