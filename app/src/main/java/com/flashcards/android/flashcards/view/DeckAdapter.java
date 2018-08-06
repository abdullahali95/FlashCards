package com.flashcards.android.flashcards.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.ViewModel.MainModel;
import com.flashcards.android.flashcards.lib.misc.JsonParser;
import com.flashcards.android.flashcards.lib.model.Deck;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

;

/**
 * Created by Abdullah Ali on 09/07/2018
 */
public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {
    private List<Deck> decks;
    private MainModel model;
    private Context context;
    private Activity activity;

    public DeckAdapter(List<Deck> decks, MainModel mainModel, Context context, Activity activity) {
        this.decks = decks;
        this.model = mainModel;
        this.context = context;
        this.activity = activity;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_rec_deck, parent, false);

        return new DeckViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeckViewHolder holder, final int position) {

        final Deck currentDeck = decks.get(position);

        // Set deck name
        holder.deckName.setText(currentDeck.getName());

        // Set cards in deck
        String totalCard = (currentDeck.getDeckSize() == 1) ? " card" : " cards";
        totalCard = currentDeck.getDeckSize() + totalCard;
        holder.cardCount.setText(totalCard);

        // Set time since last tested
        Date convertedDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String nextDue = decks.get(position).getNextTestDue();
        if (nextDue == null) holder.nextDue.setText("");
        else {
            try {
                convertedDate = dateFormat.parse(nextDue);
                String datetime;

                if (convertedDate.before(Calendar.getInstance().getTime())) {
                    // If revision due in the past, display 'now'
                    datetime = "Revision due now";
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.redBg));
                } else {
                    PrettyTime p = new PrettyTime();
                    datetime = p.format(convertedDate);
                    datetime = "Revision due " + datetime;
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                }

                holder.nextDue.setText(datetime);

            } catch (ParseException e) {
                holder.nextDue.setText(currentDeck.getLastUsed());
            } catch (Exception e) {
                holder.nextDue.setText("");
            }
        }


        holder.popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.popupButton);

                popup.inflate(R.menu.menu_main_rec);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.action_test) {
                            test(currentDeck);

                        } else if (id == R.id.action_revise) {
                            revise(currentDeck);

                        } else if (id == R.id.action_export) {
                            export(currentDeck);

                        } else if (id == R.id.action_rename) {
                            rename(currentDeck);

                        } else if (id == R.id.action_delete) {
                            delete(currentDeck);

                        }
                        return false;
                    }
                });

                popup.show();
            }
        });


    }

    public void test(Deck deck) {
        if (deck.getDeckSize() < 2) {
            // Error message
            Toast.makeText(context, "The deck must have atleast 2 cards for the test mode", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(context, TestCardActivity.class);
            intent.putExtra("deckId", deck.getDeckId());
            context.startActivity(intent);
        }
    }

    public void revise(Deck deck) {
        if (deck.getDeckSize() < 2) {
            // Error message
            Toast.makeText(context, "The deck must have atleast 2 cards for the Revise mode", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(context, ReviseCardActivity.class);
            intent.putExtra("deckId", deck.getDeckId());
            context.startActivity(intent);
        }
    }

    public void export(Deck deck) {
        File file;

        try {
            Writer output = null;
            String fileName = deck.getName();
            file = File.createTempFile(fileName, ".deck", context.getExternalCacheDir());
            ExportDeckTask task = new ExportDeckTask();
            task.execute(deck);

            // TODO: Add progress updater
            String parsed = task.get();

            output = new BufferedWriter(new FileWriter(file));
            output.write(parsed);
            output.close();
            Toast.makeText(context, "Deck saved", Toast.LENGTH_LONG).show();

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/json");
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));

            context.startActivity(Intent.createChooser(share, "Share flash cards deck"));

            //TODO: add background task to delete cache files

        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private class ExportDeckTask extends AsyncTask<Deck, Void, String> {

        @Override
        protected String doInBackground(Deck... decks) {
            String parsed = JsonParser.wrtieJson(decks[0], context);

            return parsed;
        }
    }

    private void rename(final Deck currentDeck) {
        final String[] deckName = {currentDeck.getName()};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Create a new Deck");

        // Set up the input
        final EditText input = new EditText(context);
        input.setHint(deckName[0]);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        // Set up the buttons
        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deckName[0] = input.getText().toString();
                currentDeck.setName(deckName[0]);
                model.renameDeck(currentDeck);

                // TODO: this should show up as a confirmation from Room db
                String alert = "Deck renamed";
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

    public void delete(final Deck deck) {
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
    }


    @Override
    public int getItemCount() {
        if (decks == null) {
            return 0;
        } else return decks.size();
    }

    public class DeckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        private CardView cardView;
        private TextView deckName;
        private TextView nextDue;
        private TextView cardCount;
        private ImageButton popupButton;
        private Deck deck;
        private Context context;

        public DeckViewHolder(View itemView, Context context) {
            super(itemView);
            deckName = (TextView) itemView.findViewById(R.id.title_rec_deck);
            nextDue = (TextView) itemView.findViewById(R.id.days_rec_deck);
            cardCount = (TextView) itemView.findViewById(R.id.cards_rec_deck);
            popupButton = (ImageButton) itemView.findViewById(R.id.popup_options_btn);
            cardView = (CardView) itemView.findViewById(R.id.main_rec_deck);
            this.context = context;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            deck = decks.get(getAdapterPosition());

            //Switch view
            if(v.getId() == popupButton.getId()) {
                return;

            } else {
                Intent intent = new Intent(this.context, DeckInfoActivity.class);
                intent.putExtra("Deck", deck.getDeckId());

                Pair<View, String> p1 = Pair.create((View) itemView, "deck_item_transition");

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(activity, p1);

                this.context.startActivity(intent, options.toBundle());

            }

        }

        @Override
        public boolean onLongClick(View view) {
            deck = decks.get(getAdapterPosition());

            delete(deck);

            return true;
        }
    }
}
