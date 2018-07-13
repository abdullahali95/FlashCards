package com.flashcards.android.flashcards.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.lib.Deck;
import com.flashcards.android.flashcards.view.CardEditActivity;
import com.flashcards.android.flashcards.view.TestCardActivity;

import java.util.List;

/**
 * Created by Abdullah Ali on 09/07/2018
 */
public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.ViewHolder> {
    private List<Deck> decks;
    private Context context;

    public DeckAdapter(List<Deck> decks, Context context) {
        this.decks = decks;
        this.context = context;
    }

    @NonNull
    @Override
    public DeckAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rec_deck, parent, false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckAdapter.ViewHolder holder, int position) {
        holder.deckName.setText(decks.get(position).getName());

        //TODO: format last used date properly
        holder.lastUsed.setText(decks.get(position).getLastUsed());

        String totalCard = decks.get(position).getSize() + " cards";
        holder.cardCount.setText(totalCard);

    }

    @Override
    public int getItemCount() {
        return decks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView deckName;
        private TextView lastUsed;
        private TextView cardCount;
        private Button testButton;
        private Deck deck;
        private Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            deckName = (TextView) itemView.findViewById(R.id.title_rec_deck);
            lastUsed = (TextView) itemView.findViewById(R.id.days_rec_deck);
            cardCount = (TextView) itemView.findViewById(R.id.cards_rec_deck);
            testButton = (Button) itemView.findViewById(R.id.test_button_rec_deck);
            this.context = context;

            itemView.setOnClickListener(this);
            testButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            deck = decks.get(getAdapterPosition());

            //Switch view
            if(v.getId() == testButton.getId()) {
                Intent intent = new Intent(this.context, TestCardActivity.class);
                intent.putExtra("Test Deck", deck);
                this.context.startActivity(intent);

            } else {
                Intent intent = new Intent(this.context, CardEditActivity.class);
                intent.putExtra("Deck", deck.getUuid());
                this.context.startActivity(intent);

            }

        }
    }
}
