package com.sample.androidgithubrepositories.Search;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.androidgithubrepositories.Database.DBhelper;
import com.sample.androidgithubrepositories.OpenGithubRepository.open_github_link;
import com.sample.androidgithubrepositories.R;


/**
 * Created by Adi on 27-02-2017.
 */
public class SearchResultsActivity extends AppCompatActivity {
    TextView textName, textAge;
    DBhelper myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        textName = (TextView) findViewById(R.id.textName);
        textAge = (TextView) findViewById(R.id.textAge);
        String id = getIntent().getExtras().getString("id");
        myDatabase = new DBhelper(SearchResultsActivity.this);
        if (id != null)
            showEmployeesDescription(id);
        else {
            Toast.makeText(getApplicationContext(), "Sorry! The Search value result is not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        // handleIntent(intent);
    }

    private void handleIntent(Intent intent) {


        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
        }

    }

    private void showEmployeesDescription(String id) {
        String name = "";

        Cursor cursor = myDatabase.getAllEmployeesDescription(id);
        if (cursor.getCount() < 1) {
            name += "No Data Available";

        } else {
            cursor.moveToFirst();
            do {
                name = cursor.getString(cursor.getColumnIndex(DBhelper.URL));
            }
            while (cursor.moveToNext());
        }
        Intent intent = new Intent(SearchResultsActivity.this, open_github_link.class);
        intent.putExtra("urltoopen", name);
        startActivity(intent);
        finish();
    }
}
