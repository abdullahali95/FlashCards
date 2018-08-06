package com.flashcards.android.flashcards.lib.misc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.flashcards.android.flashcards.R;
import com.flashcards.android.flashcards.data.repo.NotificationRepo;
import com.flashcards.android.flashcards.view.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Abdullah Ali on 04/08/2018
 */
public class NotificationService extends JobService {
    private boolean jobCancelled = false;
    NotificationCompat.Builder notification;
    private static final int UNIQUEID = 21213;
    private static final String CHANNEL_ID = "21213";
    private NotificationRepo repo;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("starting job", "onStartJob: ");
        NotificationTask task = new NotificationTask();
        task.execute(jobParameters);

        return true;

    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        jobFinished(jobParameters, true);
        return true;
    }

    public class NotificationTask extends AsyncTask<JobParameters, Void, Boolean> {

        @Override
        protected Boolean doInBackground(JobParameters... params) {
            Log.d("running background task", "doInBackground: ");
            if (revisionDue()) {
                createNotificationChannel();
                checkNotifications();
            }

            jobFinished(params[0], true);
            return true;
        }
    }




    /**
     * @return true if one or more decks are due for revision.
     */
    private boolean revisionDue() {
        repo = new NotificationRepo(getApplication());
        List<String> nextDue = repo.getAllNextDue();

        // If no decks have been created, return false

        Log.d("found decks", String.valueOf(nextDue == null));
        Log.d("found decks", nextDue.get(0) + " ");
        if (nextDue == null) return false;

        Log.d("found decks", "revisionDue: ");
        Date convertedDate;
        Date now = Calendar.getInstance().getTime();
        boolean revisionDue = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

        Log.d("revisionDue: ", nextDue.toString());
        Log.d("revisionDue: ", String.valueOf(nextDue.isEmpty()));
        Log.d("revisionDue: ", String.valueOf(nextDue.size()));
        for (String date : nextDue) {
            // If Deck has not been tested, move to next.
            if (date == null) continue;

            Log.d(date, "revisionDue: ");
            try {
                convertedDate = dateFormat.parse(date);
                if (convertedDate.before(now)) {
                    revisionDue = true;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return revisionDue;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "FlashCards";
            String description = "Displays notifications for flash cards due for revisions";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system;
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void checkNotifications() {
        Log.d("Background work started", "checkNotifications: ");

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notif_icon)
                .setContentTitle("Time to Revise Flash Cards")
                .setContentText("One or more of your Flash cards decks are due for revision")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("One or more of your Flash cards decks are due for revision"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(UNIQUEID, notification.build());


    }
}
