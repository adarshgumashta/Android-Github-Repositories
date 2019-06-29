package com.sample.androidgithubrepositories.Bookmarks;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentValues;
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
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sample.androidgithubrepositories.CardView.MainActivity;
import com.sample.androidgithubrepositories.Database.DBhelper;
import com.sample.androidgithubrepositories.OpenGithubRepository.open_github_link;
import com.sample.androidgithubrepositories.R;
import com.sample.androidgithubrepositories.Receiver.PrefManager;
import com.sample.androidgithubrepositories.Search.SearchResultsActivity;
import com.sample.androidgithubrepositories.filemanager.FileChooser;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Adi on 13-07-2017.
 */
public class BookmarksActivity extends AppCompatActivity implements SearchView.OnSuggestionListener {
    ListView lv;
    SQLiteDatabase newDb;
    String urltoopen;
    Intent intent;
    Bookmarks adapter;
    SearchView searchView;
    private ArrayList<String> repositorytobebookmarked = new ArrayList<>();
    private ArrayList<BookmarksCollection> results = new ArrayList<BookmarksCollection>();
    private NavigationView nvDrawer;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private PrefManager pref;
    private AdView BookmarksView;
    private ArrayList<Integer> repositorytoberemoved = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        lv = (ListView) findViewById(R.id.bookmark_list);
        DBhelper dBhelper = new DBhelper(this.getApplicationContext());
        newDb = dBhelper.getWritableDatabase();
        toolbar = (Toolbar) findViewById(R.id.toolbarlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bookmarks");
        BookmarksView = (AdView) findViewById(R.id.adView);
        AdRequest adRequestbanner = new AdRequest.Builder().build();
        BookmarksView.loadAd(adRequestbanner);
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

        // Setup drawer view
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        nvDrawer.setCheckedItem(R.id.nav_bookmarks);
        mDrawer.addDrawerListener(drawerToggle);
        pref = new PrefManager(this.getApplicationContext());
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
                Toast.makeText(getApplicationContext(), String.valueOf(isChecked), Toast.LENGTH_LONG).show();
                if (isChecked == true) {
                    pref.setshow_Notification(1);
                }
                if (isChecked == false) {
                    pref.setshow_Notification(0);
                }
            }
        });
        Cursor c = newDb.rawQuery("SELECT Name_Of_Repository,Description,URL FROM AndroidRepositories WHERE ISBOOKMARK='YES'", null);
        if (c != null) {
            while (c.moveToNext()) {
                BookmarksCollection item = new BookmarksCollection();
                item.setRepository_Name(c.getString(c.getColumnIndex("Name_Of_Repository")));
                item.setDescription(c.getString(c.getColumnIndex("Description")));
                item.setURL(c.getString(c.getColumnIndex("URL")));
                results.add(item);
            }
            adapter = new Bookmarks(this, results);
            lv.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent(BookmarksActivity.this, open_github_link.class);
                Bundle bundle = new Bundle();
                urltoopen = results.get(i).getURL();
                bundle.putString("urltoopen", results.get(i).URL);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_album, menu);
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
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.black));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_add_favourite:
                bookmarkSelectedRepositories();
                return true;
            case R.id.action_share:
                shareApp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bookmarkSelectedRepositories() {
        repositorytobebookmarked.clear();
        repositorytoberemoved.clear();
        for (int position = 0; position < lv.getAdapter().getCount(); position++) {
            View view = lv.getAdapter().getView(position, null, lv);
            BookmarksCollection lck = (BookmarksCollection) lv.getItemAtPosition(position);
            TextView tv = view.findViewById(R.id.textView2);
            if (lck.isChecked()) {
                repositorytobebookmarked.add(tv.getText().toString());
                repositorytoberemoved.add(position);
                Log.d("Monkey", tv.getText() + " is Checked!!");
            }
        }

        for (int i = 0; i < repositorytoberemoved.size(); i++) {
            BookmarksCollection item = results.get(0);
            results.remove(item);
        }
        adapter.notifyDataSetChanged();
        DBhelper dBhelper = new DBhelper(this.getApplicationContext());
        newDb = dBhelper.getWritableDatabase();
        for (String s : repositorytobebookmarked) {
            ContentValues cvs = new ContentValues();
            cvs.put("ISBOOKMARK", "NO"); //These Fields should be your String values of actual column names
            newDb.update("AndroidRepositories", cvs, "Name_Of_Repository='" + s + "'", null);
        }
        Snackbar snackbar = Snackbar.make(getCurrentFocus(), String.valueOf(repositorytobebookmarked.size()) + " repositories Removed !", Snackbar.LENGTH_LONG);
        TextView tv = snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.MAGENTA);
        snackbar.show();
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
    private void openPrivacyPolicy()
    {
        String url = "https://learnsharepointforbeginners.blogspot.com/2019/04/android-repositories-for-github.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}