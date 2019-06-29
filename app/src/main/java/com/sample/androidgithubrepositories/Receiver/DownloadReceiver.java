package com.sample.androidgithubrepositories.Receiver;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.sample.androidgithubrepositories.Bookmarks.AddBookMarkFromNotification;
import com.sample.androidgithubrepositories.Database.DBhelper;
import com.sample.androidgithubrepositories.OpenGithubRepository.open_github_link;
import com.sample.androidgithubrepositories.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Adi on 12-02-2017.
 */
public class DownloadReceiver extends BroadcastReceiver {
    private static final String tag = DownloadReceiver.class.getSimpleName();
    int randomrepository;
    SQLiteDatabase newDb;
    String repositoryName;
    String RepositoryDescription;
    String repositoryUrl;
    DownloadManager dm;

    @Override
    public void onReceive(Context context, Intent intent) {
        PrefManager pref;
        dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        pref = new PrefManager(context);
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Log.d("Marshmallow","BOOT_COMPLETEd.");
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 7);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Intent myIntentDailyNotificationPic = new Intent(context, DownloadReceiver.class);
            PendingIntent pendingIntentDailyNotificationPic = PendingIntent.getBroadcast(context, 0, myIntentDailyNotificationPic, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManagerDailyNotificationPic = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManagerDailyNotificationPic.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntentDailyNotificationPic);
        }
        if (pref.getshow_Notification() == 1) {
            if (currentHour == 7) {
                pref.setNotificationvariable(0);
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                Log.d("Marshmallow","Working.");
                if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                    NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                    if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                        if (currentHour >= 7 && currentHour <= 23) {
                            if (pref.getNotificationvariable() == 0) {
                                randomrepository = GenerateRandom();
                                DBhelper dBhelper = new DBhelper(context);
                                newDb = dBhelper.getWritableDatabase();
                                Cursor c = newDb.rawQuery("SELECT Name_Of_Repository,Description,URL FROM AndroidRepositories WHERE _id='" + randomrepository + "'", null);
                                if (c != null) {
                                    while (c.moveToNext()) {
                                        repositoryName = c.getString(c.getColumnIndex("Name_Of_Repository"));
                                        RepositoryDescription = c.getString(c.getColumnIndex("Description"));
                                        repositoryUrl = c.getString(c.getColumnIndex("URL"));
                                    }
                                    c.close();
                                }
                                newDb.close();
                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                                int notificationID = GenerateRandom();
                                int requestCode = GenerateRandom();

                                Intent intent1 = new Intent(context, open_github_link.class);
                                intent1.putExtra("fromNotification", true);
                                intent1.putExtra("urltoopen", repositoryUrl);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                PendingIntent pendingIntents = PendingIntent.getActivity(context, requestCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                                Intent intent2 = new Intent(context, AddBookMarkFromNotification.class);
                                intent2.setAction("com.Bookmark.action");
                                intent2.putExtra("notificationID", notificationID);
                                intent2.putExtra("requestCode", requestCode);
                                intent2.putExtra("repositoryName", repositoryName);
                                intent2.putExtra("urltoopen", repositoryUrl);
                                intent2.putExtra("RepositoryDescription", RepositoryDescription);
                                PendingIntent bookMarkIntents = PendingIntent.getBroadcast(context, requestCode, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

                                Intent intent3 = new Intent(Intent.ACTION_SEND);
                                intent3.setType("text/plain");
                                intent3.putExtra(Intent.EXTRA_SUBJECT, "Share this repository");
                                String sAux = "\nHey try this Repository " + repositoryName + " \n\n";
                                sAux = sAux + repositoryUrl + "\n \n \n Want to explore more repositories like this ,install " +
                                        "\n https://play.google.com/store/apps/details?id=com.sample.androidgithubrepositories";
                                intent3.putExtra(Intent.EXTRA_TEXT, sAux);
                                PendingIntent shareIntents = PendingIntent.getActivity(context, requestCode, intent3, PendingIntent.FLAG_UPDATE_CURRENT);

                                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle(repositoryName)
                                        .setSound(defaultSoundUri)
                                        .setContentText(RepositoryDescription)
                                        .setAutoCancel(true)
                                        .setContentIntent(pendingIntents);
                                builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_view, "VIEW", pendingIntents).build());
                                builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_share, "SHARE", shareIntents).build());
                                builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_input_add, "BOOKMARK", bookMarkIntents).build());
                                Notification notification = new NotificationCompat.BigTextStyle(builder).build();
                                notificationManager.notify(notificationID, notification);
                                pref.setNotificationvariable(1);
                            }
                        }
                    }
                }
            } else {
                if (currentHour >= 7 && currentHour <= 23) {
                    if (pref.getNotificationvariable() == 0) {
                        randomrepository = GenerateRandom();
                        DBhelper dBhelper = new DBhelper(context);
                        newDb = dBhelper.getReadableDatabase();
                        Cursor c = newDb.rawQuery("SELECT Name_Of_Repository,Description,URL FROM AndroidRepositories WHERE _id='" + randomrepository + "'", null);
                        if (c != null) {
                            while (c.moveToNext()) {
                                repositoryName = c.getString(c.getColumnIndex("Name_Of_Repository"));
                                RepositoryDescription = c.getString(c.getColumnIndex("Description"));
                                repositoryUrl = c.getString(c.getColumnIndex("URL"));
                            }
                            c.close();
                        }
                        newDb.close();
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                        int notificationID = GenerateRandom();
                        int requestCode = GenerateRandom();

                        Intent intent1 = new Intent(context, open_github_link.class);
                        intent1.putExtra("fromNotification", true);
                        intent1.putExtra("urltoopen", repositoryUrl);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PendingIntent pendingIntents = PendingIntent.getActivity(context, requestCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                        Intent intent2 = new Intent(context, AddBookMarkFromNotification.class);
                        intent2.setAction("com.Bookmark.action");
                        intent2.putExtra("notificationID", notificationID);
                        intent2.putExtra("requestCode", requestCode);
                        intent2.putExtra("repositoryName", repositoryName);
                        intent2.putExtra("urltoopen", repositoryUrl);
                        intent2.putExtra("RepositoryDescription", RepositoryDescription);
                        PendingIntent bookMarkIntents = PendingIntent.getBroadcast(context, requestCode, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

                        Intent intent3 = new Intent(Intent.ACTION_SEND);
                        intent3.setType("text/plain");
                        intent3.putExtra(Intent.EXTRA_SUBJECT, "Share this repository");
                        String sAux = "\nHey try this Repository " + repositoryName + " \n\n";
                        sAux = sAux + repositoryUrl + "\n \n \n Want to explore more repositories like this ,install " +
                                "\n https://play.google.com/store/apps/details?id=com.sample.androidgithubrepositories";
                        intent3.putExtra(Intent.EXTRA_TEXT, sAux);
                        PendingIntent shareIntents = PendingIntent.getActivity(context, requestCode, intent3, PendingIntent.FLAG_UPDATE_CURRENT);

                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                        String channelId = "channel-01";
                        String channelName = "Channel Name";
                        int importance = NotificationManager.IMPORTANCE_HIGH;

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            NotificationChannel mChannel = new NotificationChannel(
                                    channelId, channelName, importance);
                            notificationManager.createNotificationChannel(mChannel);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(repositoryName)
                                    .setSound(defaultSoundUri)
                                    .setContentText(RepositoryDescription)
                                    .setAutoCancel(true)
                                    .setContentIntent(pendingIntents);
                            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_view, "VIEW", pendingIntents).build());
                            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_share, "SHARE", shareIntents).build());
                            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_input_add, "BOOKMARK", bookMarkIntents).build());
                            Notification notification = new NotificationCompat.BigTextStyle(builder).build();
                            notificationManager.notify(notificationID, notification);
                        } else {
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(repositoryName)
                                    .setSound(defaultSoundUri)
                                    .setContentText(RepositoryDescription)
                                    .setAutoCancel(true)
                                    .setContentIntent(pendingIntents);
                            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_view, "VIEW", pendingIntents).build());
                            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_share, "SHARE", shareIntents).build());
                            builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_input_add, "BOOKMARK", bookMarkIntents).build());
                            Notification notification = new NotificationCompat.BigTextStyle(builder).build();
                            notificationManager.notify(notificationID, notification);
                        }
                        pref.setNotificationvariable(1);
                    }
                }
            }
        }
        String action = intent.getAction();
        String downloadedFileName = "";
        if ("android.intent.action.DOWNLOAD_COMPLETE".equals(action)) {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor c = dm.query(query);
                if (c.moveToFirst()) {
                    int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                        downloadedFileName = c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                    }
                }
            }
            unpackZip(Environment.getExternalStorageDirectory() + File.separator + "AndroidGitHubRepositories/", downloadedFileName);
        } else if ("android.intent.action.DOWNLOAD_NOTIFICATION_CLICKED".equals(action)) {
            Log.d(tag, "Notification clicked");
        } else {
//            Log.d(tag, action);
        }
    }

    public int GenerateRandom() {
        Random random = new Random();
        return random.nextInt(600);
    }

    private boolean unpackZip(String path, String zipname) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = ze.getName();

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }
                Log.d(tag, "Completed");
                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
