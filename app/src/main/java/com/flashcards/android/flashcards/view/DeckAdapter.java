package com.flashcards.android.flashcards.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.MainModel;
import com.flashcards.android.flashcards.lib.model.Deck;

import java.util.List;

/**
 * Created by Abdullah Ali on 09/07/2018
 */
public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {
    private List<Deck> decks;
    private MainModel model;
    private Context context;

    public DeckAdapter(List<Deck> decks, MainModel mainModel, Context context) {
        this.decks = decks;
        this.model = mainModel;
        this.context = context;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rec_deck, parent, false);
        return new DeckViewHolder(view, model, context);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        holder.deckName.setText(decks.get(position).getName());

        //TODO: format last used date properly
        holder.lastUsed.setText(decks.get(position).getLastUsed());

        //TODO: this probably needs data from model
        String totalCard = decks.get(position).getSize() + " cards";
        holder.cardCount.setText(totalCard);

    }

    @Override
    public int getItemCount() {
        if (decks == null) {
            return 0;
        } else return decks.size();
    }

    public class DeckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView deckName;
        private TextView lastUsed;
        private TextView cardCount;
        private Button testButton;
        private Deck deck;
        private MainModel model;
        private Context context;

        public DeckViewHolder(View itemView, MainModel model, Context context) {
            super(itemView);
            deckName = (TextView) itemView.findViewById(R.id.title_rec_deck);
            lastUsed = (TextView) itemView.findViewById(R.id.days_rec_deck);
            cardCount = (TextView) itemView.findViewById(R.id.cards_rec_deck);
            testButton = (Button) itemView.findViewById(R.id.test_button_rec_deck);
            this.model = model;
            this.context = context;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            testButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            deck = decks.get(getAdapterPosition());

            //Switch view
            if(v.getId() == testButton.getId()) {
                Intent intent = new Intent(this.context, TestCardActivity.class);
                intent.putExtra("deckId", deck.getDeckId());
                this.context.startActivity(intent);

            } else {
                Intent intent = new Intent(this.context, DeckInfoActivity.class);
                intent.putExtra("Deck", deck.getDeckId());
                this.context.startActivity(intent);

            }

        }

        @Override
        public boolean onLongClick(View view) {
            deck = decks.get(getAdapterPosition());

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Are you sure you want to delete the deck: " + deck.getName() + "?");

            // Set up the buttons
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    model.deleteDeck(deck);

                    // TODO: this should show up as a confirmation from Room db
                    String alert = "Deck: " + deck.getName() + " deleted";
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

            return true;
        }
    }
}
