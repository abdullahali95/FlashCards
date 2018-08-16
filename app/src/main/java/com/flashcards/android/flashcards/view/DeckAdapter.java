package com.flashcards.android.flashcards.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.FileProvider;
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

import com.flashcards.android.flashcards.BuildConfig;
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

/**
 * Created by Abdullah Ali on 09/07/2018
 * This is a Recycler View adapter for the list of decks displayed on the home page of the app.
 */
public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {
    private List<Deck> decks;
    private MainModel model;
    private Context context;
    private Activity activity;

    DeckAdapter(List<Deck> decks, MainModel mainModel, Context context, Activity activity) {
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
        double ls = decks.get(position).getLs();
        String infoText = "";

        // If the deck has not been revised or not yet learnt, don't show info about next revision due.
        if (nextDue == null || ls < 2) {
            infoText = "Deck learnt: " + Math.round(ls*20) + "%";
            holder.nextDue.setText(infoText);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            try {
                convertedDate = dateFormat.parse(nextDue);
                if (convertedDate.before(Calendar.getInstance().getTime())) {
                    // If revision due in the past, display 'now'
                    infoText = "Revision due now";
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.redBg));
                } else {
                    PrettyTime p = new PrettyTime();
                    infoText = p.format(convertedDate);
                    infoText = "Revision due " + infoText;
                    holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                }

                holder.nextDue.setText(infoText);

            } catch (ParseException e) {
                holder.nextDue.setText(currentDeck.getLastUsed());
            } catch (Exception e) {
                holder.nextDue.setText(infoText);
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

                        // Switch statements more efficient then nested if
                        switch (id) {
                            case R.id.action_test:
                                test(currentDeck);

                                break;
                            case R.id.action_revise:
                                revise(currentDeck);

                                break;
                            case R.id.action_export:
                                export(currentDeck);

                                break;
                            case R.id.action_rename:
                                rename(currentDeck);

                                break;
                            case R.id.action_delete:
                                delete(currentDeck);

                                break;
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
            Toast.makeText(context, "The deck must have atleast 2 cards for the evise mode", Toast.LENGTH_LONG).show();
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

            String parsed = task.get();
            output = new BufferedWriter(new FileWriter(file));
            output.write(parsed);
            output.close();

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("application/deck");
            Uri exportedFile = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);

            share.putExtra(Intent.EXTRA_STREAM, exportedFile);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(share, "Share flash cards deck"));


        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    private class ExportDeckTask extends AsyncTask<Deck, Void, String> {

        @Override
        protected String doInBackground(Deck... decks) {
            return JsonParser.wrtieJson(decks[0], context);
        }
    }

    private void rename(final Deck currentDeck) {
        final String[] deckName = {currentDeck.getName()};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Rename Deck");

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
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                model.deleteDeck(deck);
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

        AlertDialog alert = builder.show();

        alert.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(context.getResources().getColor(R.color.redBg));
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.white));
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

        DeckViewHolder(View itemView, Context context) {
            super(itemView);
            deckName = itemView.findViewById(R.id.title_rec_deck);
            nextDue = itemView.findViewById(R.id.days_rec_deck);
            cardCount = itemView.findViewById(R.id.cards_rec_deck);
            popupButton = itemView.findViewById(R.id.popup_options_btn);
            cardView = itemView.findViewById(R.id.main_rec_deck);
            this.context = context;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            deck = decks.get(getAdapterPosition());
            //Switch view
            if(v.getId() != popupButton.getId()) {
                Intent intent = new Intent(this.context, DeckInfoActivity.class);
                intent.putExtra("Deck", deck.getDeckId());
                Pair<View, String> p1 = Pair.create(itemView, "deck_item_transition");
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
