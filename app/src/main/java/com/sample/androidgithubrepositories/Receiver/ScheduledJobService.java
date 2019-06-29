package com.sample.androidgithubrepositories.Receiver;

public class ScheduledJobService{} /*extends JobService {
    int randomrepository;
    SQLiteDatabase newDb;
    String repositoryName;
    String RepositoryDescription;
    String repositoryUrl;

    @Override
    public boolean onStartJob(JobParameters job) {
        //Log.d("ScheduledJobService", "Job called");
        //Toast.makeText( getApplicationContext(), "hi", Toast.LENGTH_LONG).show();
        PrefManager pref;
        pref = new PrefManager(getApplicationContext());
        Calendar rightNow = Calendar.getInstance();

        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext()
                .getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
        *//*Notification.Builder builder2 = new Notification.Builder(getApplicationContext());
        builder2.setContentTitle("Logical Conditions")
                .setContentText("Job called isNetworkConnected" +String.valueOf(networkInfo.isConnected())+"String.valueOf(pref.getNotificationvariable()).equals(\"0\")->" + String.valueOf(pref.getNotificationvariable()).equals("0") +"currentHour >= 7 && currentHour <= 22"+ String.valueOf (currentHour >= 7 && currentHour <= 22))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher);
        Notification notification2 = new Notification.BigTextStyle(builder2).build();
        NotificationManager notificationManager2 = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager2.notify(1000, notification2);*//*
        if (networkInfo.isConnected()) {
            if (currentHour >= 7 && currentHour <= 23) {
                if (pref.getNotificationvariable()==0) {
                    randomrepository = GenerateRandom();
                    DBhelper dBhelper = new DBhelper(getApplicationContext());
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
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
                    int notificationID = GenerateRandom();
                    int requestCode = GenerateRandom();

                    Intent intent1 = new Intent(getApplicationContext(), open_github_link.class);
                    intent1.putExtra("fromNotification", true);
                    intent1.putExtra("urltoopen", repositoryUrl);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent pendingIntents = PendingIntent.getActivity(this, requestCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent intent2 = new Intent(getApplicationContext(), AddBookMarkFromNotification.class);
                    intent2.setAction("com.Bookmark.action");
                    intent2.putExtra("notificationID", notificationID);
                    intent2.putExtra("requestCode", requestCode);
                    intent2.putExtra("repositoryName", repositoryName);
                    intent2.putExtra("urltoopen", repositoryUrl);
                    intent2.putExtra("RepositoryDescription", RepositoryDescription);
                    PendingIntent bookMarkIntents = PendingIntent.getBroadcast(this, requestCode, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

                    Intent intent3 = new Intent(Intent.ACTION_SEND);
                    intent3.setType("text/plain");
                    intent3.putExtra(Intent.EXTRA_SUBJECT, "Share this repository");
                    String sAux = "\nHey try this Repository " + repositoryName + " \n\n";
                    sAux = sAux + repositoryUrl + "\n \n \n Want to explore more repositories like this ,install " +
                            "\n https://play.google.com/store/apps/details?id=com.sample.androidgithubrepositories";
                    intent3.putExtra(Intent.EXTRA_TEXT, sAux);
                    PendingIntent shareIntents = PendingIntent.getActivity(this, requestCode, intent3, PendingIntent.FLAG_UPDATE_CURRENT);

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



                    String channelId = "channel-01";
                    String channelName = "Channel Name";
                    int importance = NotificationManager.IMPORTANCE_HIGH;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel mChannel = new NotificationChannel(
                                channelId, channelName, importance);
                        notificationManager.createNotificationChannel(mChannel);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channelId)
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
                    else
                    {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
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
                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
                    dispatcher.cancelAll();
                }
            }
        }
        return false;
    }

    public int GenerateRandom() {
        Random random = new Random();
        return random.nextInt(600);
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
*/