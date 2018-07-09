package com.flashcards.android.flashcards.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.lib.Deck;

import java.util.List;

/**
 * Created by abdul on 09/07/2018
 */
public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.ViewHolder> {
    private List<Deck> decks;

    public DeckAdapter(List<Deck> decks) {
        this.decks = decks;
    }

    @NonNull
    @Override
    public DeckAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rec_deck, parent, false);
        return new ViewHolder(view);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView deckName;
        public TextView lastUsed;
        public TextView cardCount;

        public ViewHolder(View itemView) {
            super(itemView);
            deckName = itemView.findViewById(R.id.title_rec_deck);
            lastUsed = itemView.findViewById(R.id.days_rec_deck);
            cardCount = itemView.findViewById(R.id.cards_rec_deck);



        }
    }
}
