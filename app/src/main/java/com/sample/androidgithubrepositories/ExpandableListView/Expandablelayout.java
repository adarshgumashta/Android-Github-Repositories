package com.sample.androidgithubrepositories.ExpandableListView;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.sample.androidgithubrepositories.Bookmarks.BookmarksActivity;
import com.sample.androidgithubrepositories.CardView.MainActivity;
import com.sample.androidgithubrepositories.Database.DBhelper;
import com.sample.androidgithubrepositories.R;
import com.sample.androidgithubrepositories.Receiver.PrefManager;
import com.sample.androidgithubrepositories.Search.SearchResultsActivity;
import com.sample.androidgithubrepositories.filemanager.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Expandablelayout extends AppCompatActivity implements SearchView.OnSuggestionListener {


    private static HashMap<String, ArrayList<Boolean>> itemStateArray2 = new HashMap<>();
    SQLiteDatabase newDb;
    String parentTopicName = "";
    SearchView searchView;
    RecyclerView recyclerView;
    private MultiCheckGenreAdapter adapter;
    private PrefManager pref;
    private NavigationView nvDrawer;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<String> repositorytobebookmarked = new ArrayList<>();
    private AdView ExpandablelayoutView;
    private InterstitialAd mInterstitialAd;

    public static HashMap<String, ArrayList<Boolean>> getItemStateArray2() {
        return itemStateArray2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandablelayout);
        parentTopicName = getIntent().getExtras().getString("parenttopicName");
        toolbar = (Toolbar) findViewById(R.id.toolbarexpandable);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(parentTopicName);
        ExpandablelayoutView = (AdView) findViewById(R.id.adView);
        AdRequest adRequestbanner = new AdRequest.Builder().build();
        ExpandablelayoutView.loadAd(adRequestbanner);
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        nvDrawer.setItemIconTintList(null);
        nvDrawer.getMenu().findItem(R.id.nav_home).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_bookmarks).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_fileexplorer).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        //nvDrawer.getMenu().findItem(R.id.nav_notification).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_appdata).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_share).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_Rate).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_more).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_About).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);
        nvDrawer.getMenu().findItem(R.id.nav_PP).getIcon().setColorFilter(Color.parseColor("#FFFF4081"), PorterDuff.Mode.SRC_IN);

        // Setup drawer view
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        pref = new PrefManager(this.getApplicationContext());
        // MenuItem switchItem = nvDrawer.getMenu().findItem(R.id.nav_notification);
        /*CompoundButton switchView = (CompoundButton) MenuItemCompat.getActionView(switchItem);
        if (pref.getshow_Notification() == 1) {
            switchView.setChecked(true);
        }
        if (pref.getshow_Notification() == 0) {
            switchView.setChecked(false);
        }
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getApplicationContext(), String.valueOf(isChecked), Toast.LENGTH_LONG).show();
                if (isChecked == true) {
                    pref.setshow_Notification(1);
                }
                if (isChecked == false) {
                    pref.setshow_Notification(0);
                }
            }
        });*/
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new MultiCheckGenreAdapter(SetParentRepositories());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        // Load ads into Interstitial Ads

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private List<MultiCheckGenre> SetParentRepositories() {
        List<MultiCheckGenre> group_list = new ArrayList<>();
        DBhelper dBhelper = new DBhelper(this.getApplicationContext());
        newDb = dBhelper.getWritableDatabase();
        Cursor parentcursor = newDb.rawQuery("SELECT Sub_Topic_Name FROM AndroidRepositories WHERE Topic_Name='" + parentTopicName + "'", null);
        String list;
        List<String> stringList = new ArrayList<>();
        if (parentcursor != null) {

            while (parentcursor.moveToNext()) {
                Log.d("Monkey", parentcursor.getString(parentcursor.getColumnIndex("Sub_Topic_Name")));
                list = parentcursor.getString(parentcursor.getColumnIndex("Sub_Topic_Name"));
                if (!stringList.contains(list)) {
                    stringList.add(list);
                    //group_list.add(new MultiCheckGenre(list, getsubtopic(list), R.drawable.ic_electric_guitar));
                    group_list.add(new MultiCheckGenre(list, getsubtopic(list)));
                }
            }
        }
        parentcursor.close();
        return group_list;
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ClassName", "Expandable Layout");
    }

    protected ArrayList<Artist> getsubtopic(String no) {
        ArrayList<Artist> artistArrayList = new ArrayList<>();
        ArrayList<Boolean> isBooleanArrayList = new ArrayList<>();
        Cursor childcursor = newDb.rawQuery("SELECT Name_Of_Repository,Description,URL,ISBOOKMARK FROM AndroidRepositories WHERE Sub_Topic_Name='" + no + "' and Topic_Name='" + parentTopicName + "'", null);
        if (childcursor != null && childcursor.moveToFirst()) {
            do {
                Artist billMonroe = new Artist(childcursor.getString(childcursor.getColumnIndex("Name_Of_Repository")), childcursor.getString(childcursor.getColumnIndex("Description")), childcursor.getString(childcursor.getColumnIndex("URL")), no);
                artistArrayList.add(billMonroe);
                if (childcursor.getString(childcursor.getColumnIndex("ISBOOKMARK")) != null && childcursor.getString(childcursor.getColumnIndex("ISBOOKMARK")).equalsIgnoreCase("Yes"))
                    isBooleanArrayList.add(true);
                else
                    isBooleanArrayList.add(false);
            } while (childcursor.moveToNext());
        }
        childcursor.close();
        itemStateArray2.put(no, isBooleanArrayList);
        return artistArrayList;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_album, menu);
        MenuItem item = menu.findItem(R.id.action_add_favourite);
        item.setVisible(false);
        doSearchActivateSearch(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void doSearchActivateSearch(Menu menu) {

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
        searchView.setOnSuggestionListener(this);
        EditText searchEditText = (EditText) searchView.findViewById(androidx.appcompat.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.black));

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
  /*  private void RepositoriestobeBookmark() {
        repositorytobebookmarked.clear();
        ArrayList<Artist> arrayList;
        MultiCheckGenreAdapter adapter1 = (MultiCheckGenreAdapter) recyclerView.getAdapter();
        for (int position = 0; position < adapter1.getGroups().size(); position++) {
            MultiCheckGenre checkGenre = (MultiCheckGenre) adapter1.getGroups().get(position);
            int childCount = checkGenre.getItemCount();
            for (int j = 0; j < childCount; j++) {
                if (checkGenre.isChildChecked(j)) {
                    arrayList = (ArrayList<Artist>) checkGenre.getItems();
                    repositorytobebookmarked.add(arrayList.get(j).getChild_Repository_Name());
                }
            }
        }
        DBhelper dBhelper = new DBhelper(this.getApplicationContext());
        newDb = dBhelper.getWritableDatabase();
        for (String s : repositorytobebookmarked) {
            ContentValues cvs = new ContentValues();
            cvs.put("ISBOOKMARK", "YES"); //These Fields should be your String values of actual column names
            newDb.update("AndroidRepositories", cvs, "Name_Of_Repository='" + s + "'", null);
        }
        Snackbar snackbar = Snackbar.make(getCurrentFocus(), String.valueOf(repositorytobebookmarked.size()) + " repositories Bookmarked !", Snackbar.LENGTH_LONG);
        TextView tv = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.MAGENTA);
        snackbar.show();
    }*/

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
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
           /* case R.id.nav_notification:
                break;*/
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

    @Override
    public void onBackPressed() {
        pref = new PrefManager(this);
        pref.setShowaddvar(pref.getShowaddvar()+1);
        final AdRequest adRequest = new AdRequest.Builder()
                .build();
        if ((pref.getShowaddvar() % 7 == 0)) {
            mInterstitialAd.loadAd(adRequest);
        }
        finish();
        Intent intent = new Intent(Expandablelayout.this, MainActivity.class);
        startActivity(intent);
    }

    private void openPrivacyPolicy() {
        String url = "https://learnsharepointforbeginners.blogspot.com/2019/04/android-repositories-for-github.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}