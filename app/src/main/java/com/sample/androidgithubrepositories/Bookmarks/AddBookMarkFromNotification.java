package com.sample.androidgithubrepositories.Bookmarks;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;

import com.sample.androidgithubrepositories.Database.DBhelper;
import com.sample.androidgithubrepositories.OpenGithubRepository.open_github_link;
import com.sample.androidgithubrepositories.R;

public class AddBookMarkFromNotification extends BroadcastReceiver {
    SQLiteDatabase newDb;
    String repositoryName;
    int notificationID;
    int requestCode;
    String repositoryUrl;
    String RepositoryDescription;
    @Override
    public void onReceive(Context context, Intent intent) {
        repositoryName = intent.getExtras().getString("repositoryName");
        notificationID = intent.getExtras().getInt("notificationID");
        requestCode = intent.getExtras().getInt("notificationID");
        repositoryUrl = intent.getExtras().getString("urltoopen");
        RepositoryDescription = intent.getExtras().getString("RepositoryDescription");
        DBhelper dBhelper = new DBhelper(context);
        newDb = dBhelper.getWritableDatabase();
        newDb.beginTransaction();
        ContentValues cvs = new ContentValues();
        cvs.put("ISBOOKMARK", "YES"); //These Fields should be your String values of actual column names
        newDb.update("AndroidRepositories", cvs, "Name_Of_Repository='" + repositoryName + "'", null);
        newDb.setTransactionSuccessful();
        newDb.endTransaction();
        newDb.close();

        Intent intent1 = new Intent( context, open_github_link.class);
        intent1.putExtra("fromNotification", true);
        intent1.putExtra("urltoopen", repositoryUrl);
        intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntents = PendingIntent.getActivity( context, requestCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent intent3 = new Intent(Intent.ACTION_SEND);
        intent3.setType("text/plain");
        intent3.putExtra(Intent.EXTRA_SUBJECT, "Share this repository");
        String sAux = "\nHey try this Repository "+repositoryName +" \n\n";
        sAux = sAux + repositoryUrl + "\n \n \n Want to explore more repositories like this ,install "+
                "\n https://play.google.com/store/apps/details?id=com.sample.androidgithubrepositories" ;
        intent3.putExtra(Intent.EXTRA_TEXT, sAux);
        PendingIntent shareIntents = PendingIntent.getActivity( context, requestCode, intent3, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationManager notificationManager = (NotificationManager)  context.getSystemService( context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(repositoryName)
                    .setContentText(RepositoryDescription)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntents);
            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_view, "VIEW", pendingIntents).build());
            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_share, "SHARE", shareIntents).build());
            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_input_add, "BOOKMARKED", null).build());
            Notification notification = new NotificationCompat.BigTextStyle(builder).build();
            notificationManager.notify(notificationID, notification);
        }
        else
        {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(repositoryName)
                    .setContentText(RepositoryDescription)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntents);
            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_view, "VIEW", pendingIntents).build());
            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_share, "SHARE", shareIntents).build());
            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_input_add, "BOOKMARKED", null).build());
            Notification notification = new NotificationCompat.BigTextStyle(builder).build();
            notificationManager.notify(notificationID, notification);
        }
    }
}
