package com.sample.androidgithubrepositories.CardView;


import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.kobakei.ratethisapp.RateThisApp;
import com.sample.androidgithubrepositories.Bookmarks.BookmarksActivity;
import com.sample.androidgithubrepositories.Database.DBAdapter;
import com.sample.androidgithubrepositories.R;
import com.sample.androidgithubrepositories.Receiver.DownloadReceiver;
import com.sample.androidgithubrepositories.Receiver.PrefManager;
import com.sample.androidgithubrepositories.Search.SearchResultsActivity;
import com.sample.androidgithubrepositories.Utils;
import com.sample.androidgithubrepositories.filemanager.FileChooser;
import com.sample.androidgithubrepositories.onBoarding;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SearchView.OnSuggestionListener {

    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    /*Variables for Providing Permissions*/
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private static final Intent[] AUTO_START_INTENTS = {
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent("miui.intent.action.OP_AUTO_START").addCategory(Intent.CATEGORY_DEFAULT),
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.entry.FunctionActivity")).setData(
                    Uri.parse("mobilemanager://function/entry/AutoStart"))
    };
    SearchView searchView;
    PrefManager pref;
    InterstitialAd mInterstitialAd;
    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    private List<String> topic = new ArrayList<>();
    private NavigationView nvDrawer;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private boolean isUserFirstTime;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;


    public static void setAlarm(Context context) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Intent myIntent = new Intent(context, DownloadReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 4*AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));

        Intent introIntent = new Intent(MainActivity.this, onBoarding.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);
        if (isUserFirstTime)
            startActivity(introIntent);
        setContentView(R.layout.activity_main);
        pref = new PrefManager(this.getApplicationContext());
        toolbar = (Toolbar) findViewById(R.id.toolbarmain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        final WeakReference<MainActivity> mContext;
        mContext = new WeakReference<>(MainActivity.this);

        if (pref.getSetAlarmOnce() == 1) {
            if (pref.getshow_Notification() == 0 && pref.getSwapSwitchOnce() == 0) {
                    pref.setshow_Notification(1);
                    pref.setSwapSwitchOnce(1);
            }
            if(pref.getOneMoreAlarm()==0)
            {
                setAlarm(mContext.get());
                pref.setOneMoreAlarm(1);
                pref.setshow_Notification(1);
                pref.setSwapSwitchOnce(1);
            }
            if(pref.getTwoMoreAlarm()==1)
            {
                setAlarm(mContext.get());
                pref.setOneMoreAlarm(1);
                pref.setTwoMoreAlarm(1);
                pref.setshow_Notification(1);
                pref.setSwapSwitchOnce(1);
            }

        }

        /*for newly installed app*/
        if (pref.getSetAlarmOnce() == 0) {
            pref.setshow_Notification(1);
            myAsyncTask mTask = new myAsyncTask(mContext);
            mTask.execute("abc", "10", "Hello world");
            pref.setSetAlarmOnce(1);
            pref.setSwapSwitchOnce(1);
        }
        /*for newly installed app*/


        // Monitor launch times and interval from installation
        // Custom condition: 3 days and 5 launches
        RateThisApp.Config config = new RateThisApp.Config(3, 10);
        RateThisApp.init(config);
        RateThisApp.onCreate(this);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(MainActivity.this, R.style.MyAlertDialogStyle2);
        RateThisApp.setCallback(new RateThisApp.Callback() {
            @Override
            public void onYesClicked() {
                RateThisApp.stopRateDialog(getApplicationContext());
            }

            @Override
            public void onNoClicked() {
                RateThisApp.stopRateDialog(getApplicationContext());
            }

            @Override
            public void onCancelClicked() {
            }
        });
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        nvDrawer.setItemIconTintList(null);
        nvDrawer.getMenu().findItem(R.id.nav_home).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_bookmarks).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_fileexplorer).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_notification).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_appdata).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_share).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_Rate).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_more).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_About).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);

        nvDrawer.setCheckedItem(R.id.nav_home);
        MenuItem switchItem = nvDrawer.getMenu().findItem(R.id.nav_notification);
        CompoundButton switchView = (CompoundButton) MenuItemCompat.getActionView(switchItem);
        if (pref.getshow_Notification() == 1) {
            switchView.setChecked(true);
        }
        if (pref.getshow_Notification() == 0) {
            switchView.setChecked(false);
        }
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pref.setshow_Notification(1);
                }
                if (!isChecked) {
                    pref.setshow_Notification(0);
                }
            }
        });
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        ProvidePermissions();
        prepareAlbums();
    }

    private void ProvidePermissions() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        //  Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_LONG).show();
    }

    private void prepareAlbums() {
        int[] noOfRepositories = new int[]{14, 71, 15, 7, 81, 27, 12, 22, 67, 37, 10, 5, 41, 9, 18, 31, 11, 4, 7, 6, 11, 6, 4, 13, 24, 19, 32, 9, 3, 15, 72, 6};
        int[] covers = new int[]{
                R.drawable.material,
                R.drawable.imageview,
                R.drawable.viewpager,
                R.drawable.actionbar,
                R.drawable.listview,
                R.drawable.animation,
                R.drawable.database,
                R.drawable.ui,
                R.drawable.androidcode,
                R.drawable.music,
                R.drawable.appintro,
                R.drawable.elements,
                R.drawable.systemlibraries,
                R.drawable.maps,
                R.drawable.http,
                R.drawable.view,
                R.drawable.dialog,
                R.drawable.status,
                R.drawable.pulltorefresh,
                R.drawable.snackbar,
                R.drawable.swipe,
                R.drawable.notifications,
                R.drawable.navigation,
                R.drawable.floatingactionbutton,
                R.drawable.progressbar,
                R.drawable.datetimepicker,
                R.drawable.edittext,
                R.drawable.chart,
                R.drawable.search,
                R.drawable.utilities,
                R.drawable.other,
                R.drawable.games
        };
        topic.add("Material Design Examples");
        topic.add("ImageView");
        topic.add("ViewPager");
        topic.add("ActionBar");
        topic.add("ListView");
        topic.add("Animations");
        topic.add("Database");
        topic.add("UI/UX");
        topic.add("Android Full Codes and Code Related");
        topic.add("Music and Video and camera");
        topic.add("App Intro");
        topic.add("Elements");
        topic.add("Android System Libraries");
        topic.add("Maps");
        topic.add("HTTP Libraries");
        topic.add("View");
        topic.add("Dialog and Alerts");
        topic.add("Status Bar");
        topic.add("PulltoRefresh Libraries");
        topic.add("SnackBar and Toast");
        topic.add("Swipe");
        topic.add("Notifications");
        topic.add("Navigation Drawer");
        topic.add("Floating Action Button and Button");
        topic.add("Progress Bar");
        topic.add("Date,Time,Calender and other pickers");
        topic.add("TextView and EditText");
        topic.add("Charts Libraries");
        topic.add("Search Bar");
        topic.add("Utilities Tools");
        topic.add("Other Library");
        topic.add("Games");
        Album a;
        for (int i = 0; i < covers.length; i++) {
            a = new Album(topic.get(i), noOfRepositories[i], covers[i]);
            albumList.add(a);
        }
        adapter.notifyDataSetChanged();
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album, menu);
        menu.findItem(R.id.action_add_favourite).setVisible(false);
        doSearchActivateSearch(menu);

        return true;
    }

    private void doSearchActivateSearch(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
        searchView.setOnSuggestionListener(this);
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.black));
        //  searchView.setIconifiedByDefault(false);
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return true;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().
                getItem(position);
        String suggest1 = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));

        Intent intent = new Intent(this, SearchResultsActivity.class);

        intent.putExtra("id", suggest1);

        startActivity(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_share:
                shareApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                setTitle(menuItem.getTitle());
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_About:
                showAbout();
                break;
            /*case R.id.nav_addFree:
                break;*/
            case R.id.nav_appdata:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Clear App Data?");
                alertDialog.setMessage("Do you Really want to clear Application Data ?This will clear App Data but will not delete Databases!");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearApplicationData(getApplicationContext());
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                break;
            case R.id.nav_bookmarks:
                setTitle(menuItem.getTitle());
                intent = new Intent(this, BookmarksActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_fileexplorer:
                setTitle(menuItem.getTitle());
                intent = new Intent(this, FileChooser.class);
                startActivity(intent);
                break;
            case R.id.nav_more:
                openPlayStore("market://search?q=pub:adarshgumashta");
                break;
            case R.id.nav_notification:
                break;
            case R.id.nav_Rate:
                openPlayStore("market://details?id=com.sample.androidgithubrepositories");
                break;
            case R.id.nav_share:
                shareApp();
                break;
            case R.id.nav_wallistract:
                openPlayStore("market://details?id=com.labstract.lest.wallistract");
                break;
            case R.id.nav_VDFb:
                openPlayStore("market://details?id=com.video.fb.facebookvideodownloader");
                break;
            case R.id.nav_VDFbPro:
                openPlayStore("market://details?id=com.video.fb.facebookvideodownloaderpaid");
                break;
            case R.id.nav_RJMS:
                openPlayStore("market://details?id=com.chit.adarsh.rajamantrichorsipahi");
                break;
            case R.id.nav_PP:
                openPrivacyPolicy();
                break;
        }
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    public void showAbout() {
        // Inflate the about message contents
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        TextView textView = messageView.findViewById(R.id.title);
        TextView textView1 = messageView.findViewById(R.id.about_credits);
        TextView textView2 = messageView.findViewById(R.id.textView2);
        textView.setTextColor(Color.DKGRAY);
        textView1.setTextColor(Color.DKGRAY);
        textView2.setTextColor(Color.DKGRAY);
        String tex = "Android github repositories enables you to browse,search,download over 700 Android repositories.You can share it with your colleagues,friends over whatsapp ,mail . Any Unauthorized downloading or re-uploading of contents and/or violations of intellectual property rights is the sole responsibility of the user .\n \nHappy Browsing :) ";
        textView2.setText(tex);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    public void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                File f = new File(appDir, s);
                if (deleteDir(f))
                    Log.i("ClearApplicationDAta", String.format("**************** DELETED -> (%s) *******************", f.getAbsolutePath()));
            }
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (!(dir.toString().contains("shared_prefs") || dir.toString().contains("databases"))) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    public void shareApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Android Github Repositories");
            String sAux = "\nDo you want to browse Android Github Repositories ? .Install this App , Its Amazing :). \n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=com.sample.androidgithubrepositories";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Share this App"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    private void openPlayStore(String appLink) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(appLink));
        startActivity(intent);
    }

    private void openPrivacyPolicy()
    {
        String url = "https://learnsharepointforbeginners.blogspot.com/2019/04/android-repositories-for-github.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public static class myAsyncTask extends AsyncTask<String, Integer, String> {
        String mTAG = "myAsyncTask";
        ProgressDialog progDailog;
        private WeakReference<MainActivity> mContext;

        public myAsyncTask(WeakReference<MainActivity> context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            progDailog = new ProgressDialog(mContext.get());
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected String doInBackground(String... arg) {
            try {
                DBAdapter dbAdapter = new DBAdapter(mContext.get());
                dbAdapter.insertData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = "";
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            setAlarm(mContext.get());
            if(progDailog!=null)
            {
                progDailog.dismiss();
            }

            try {
                for (final Intent intent : AUTO_START_INTENTS)
                    if (mContext.get().getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                        AlertDialog.Builder alertDialog;
                        alertDialog = new AlertDialog.Builder(mContext.get());
                        alertDialog.setTitle("Enable Autostart?");
                        alertDialog.setMessage("Please Enable autostart for This App to get daily notifications.");
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mContext.get().startActivity(intent);
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                        break;
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(mTAG, "Inside onPostExecute");
        }

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
