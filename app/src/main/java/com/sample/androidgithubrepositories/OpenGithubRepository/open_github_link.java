package com.sample.androidgithubrepositories.OpenGithubRepository;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.sample.androidgithubrepositories.Bookmarks.BookmarksActivity;
import com.sample.androidgithubrepositories.CardView.MainActivity;
import com.sample.androidgithubrepositories.R;
import com.sample.androidgithubrepositories.Receiver.PrefManager;
import com.sample.androidgithubrepositories.Search.SearchResultsActivity;
import com.sample.androidgithubrepositories.filemanager.FileChooser;

import java.io.File;

public class open_github_link extends AppCompatActivity implements SearchView.OnSuggestionListener {
    Bundle bundle;
    WebView mWebview;
    SearchView searchView;
    int isInternetConnected = 1;
    String urltoopen;
    boolean fromNotification = false;
    private ProgressBar mprogress;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private PrefManager pref;
    private FloatingActionButton download, share, reload;
    private NavigationView nvDrawer;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private AdView open_github_linkView;

    public static boolean readAndWriteExternalStorage(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        } else {
            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_github_link);
        urltoopen = getIntent().getExtras().getString("urltoopen");
        fromNotification = getIntent().getExtras().getBoolean("fromNotification");
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        download = (FloatingActionButton) findViewById(R.id.downloadfab);
        share = (FloatingActionButton) findViewById(R.id.sharefab);
        reload = (FloatingActionButton) findViewById(R.id.reload);
        mprogress = (ProgressBar) findViewById(R.id.progressBar);
        mWebview = (WebView) findViewById(R.id.webView);
        toolbar = (Toolbar) findViewById(R.id.toolbarwebview);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(urltoopen);
        open_github_linkView = (AdView) findViewById(R.id.adView);
        AdRequest adRequestbanner = new AdRequest.Builder().build();
        open_github_linkView.loadAd(adRequestbanner);

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
                //   Toast.makeText(getApplicationContext(),String.valueOf(isChecked),Toast.LENGTH_LONG).show();
                if (isChecked == true) {
                    pref.setshow_Notification(1);
                }
                if (isChecked == false) {
                    pref.setshow_Notification(0);
                }
            }
        });
        mprogress.setProgress(0);
        mprogress.setMax(100);
        mWebview.setVisibility(View.INVISIBLE);
        mWebview.getSettings().setSupportZoom(true); //Zoom Control on web (You don't need this
        mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    isInternetConnected = 1;
                    if (mWebview.getUrl() == null)
                        mWebview.loadUrl(urltoopen);
                    else {
                        //mWebview.reload();
                        mWebview.loadUrl(mWebview.getUrl());
                        Toast.makeText(open_github_link.this, "Please wait while page reloads!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    isInternetConnected = 0;
                    Toast.makeText(open_github_link.this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Share this repository");
                    String sAux = "\nHey try this Repositories ? . Its Amazing :). \n\n";
                    sAux = sAux + urltoopen;
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Share this App"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "AndroidGitHubRepositories");
                directory.mkdirs();
                if (urltoopen != null) {
                    String downloadurl = urltoopen.concat("/archive/master.zip");
                    String fileName = urltoopen.substring(urltoopen.lastIndexOf('/') + 1);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadurl));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    File root = new File(Environment.getExternalStorageDirectory() + File.separator + "AndroidGitHubRepositories");
                    Uri path = Uri.withAppendedPath(Uri.fromFile(root), fileName);
                    request.setDestinationUri(path);
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    if (readAndWriteExternalStorage(open_github_link.this)) {
                        dm.enqueue(request);
                    } else {
                        Toast.makeText(open_github_link.this, "Download Permission Not Provided!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 19) {
            mWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mprogress.setProgress(0);
                        mprogress.setMax(100);
                        if (mprogress.getProgress() == 0) {
                            mprogress.setVisibility(View.VISIBLE);
                            mWebview.setVisibility(View.INVISIBLE);
                            // Toast.makeText(getApplicationContext(), urltoopen, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 0);
            }

            public void onPageFinished(WebView view, final String url) {
                super.onPageFinished(view, url);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mprogress.getProgress() == 100) {
                            mprogress.setVisibility(View.INVISIBLE);
                            mWebview.setVisibility(View.VISIBLE);
                            mySwipeRefreshLayout.setRefreshing(false);
                            // Toast.makeText(getApplicationContext(), urltoopen, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 2000);
                // do your javascript injection here, remember "javascript:" is needed to recognize this code is javascript

            }
        });
        mWebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP) {
                    if (mWebview.canGoBack()) {
                        mWebview.goBack();
                    } else {
                        if(fromNotification)
                        {
                            Intent intent;
                            intent = new Intent(open_github_link.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            finish();
                        }
                    }
                }
                return true;
            }
        });
        mWebview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                mprogress.setProgress(progress);
    /*  getParent().setTitle("Loading...");
      getParent().setProgress(progress * 100);*/
                //Make the bar disappear after URL is loaded
                // Return the app name after finish loading
                if (progress == 100) {
                    /*getParent().setTitle(R.string.app_name);*/
                }
            }
        });
        if (isNetworkAvailable()) {
            isInternetConnected = 1;
            mWebview.loadUrl(urltoopen);
        } else {
            isInternetConnected = 0;
            mySwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(open_github_link.this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
        }

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    isInternetConnected = 1;
                    //mWebview.reload();
                    if (mWebview.getUrl() == null)
                        mWebview.loadUrl(urltoopen);
                    else {
                        mWebview.loadUrl(mWebview.getUrl());
                    }
                } else {
                    isInternetConnected = 0;
                    mySwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(open_github_link.this, "Please Connect to Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ClassName", "open_github_link");
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_album, menu);
        menu.findItem(R.id.menu_search).setVisible(false);
        menu.findItem(R.id.action_add_favourite).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
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

    private void openPrivacyPolicy() {
        String url = "https://learnsharepointforbeginners.blogspot.com/2019/04/android-repositories-for-github.html";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}