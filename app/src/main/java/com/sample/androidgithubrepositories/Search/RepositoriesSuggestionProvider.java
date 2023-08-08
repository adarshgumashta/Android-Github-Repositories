package com.sample.androidgithubrepositories.Search;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import com.sample.androidgithubrepositories.Database.DBhelper;

import java.util.ArrayList;

/**
 * Created by Adi on 03-03-2017.
 */
public class RepositoriesSuggestionProvider extends ContentProvider {
    DBhelper myDB;
    ArrayList<String> employess;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        myDB = new DBhelper(getContext());

        String name = null;
        employess = myDB.getAllName();
        MatrixCursor matrixCursor = new MatrixCursor(
                new String[]{
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
                }
        );
        if (employess != null) {
            String query = uri.getLastPathSegment().toString();
            int limit = Integer.parseInt(uri.getQueryParameter(
                    SearchManager.SUGGEST_PARAMETER_LIMIT));

            int length = employess.size();

            for (int i = 0; i < length && matrixCursor.getCount() < limit; i++) {
                String recipe = employess.get(i);
                if (recipe.contains(query)) {
                    matrixCursor.addRow(new Object[]{i, recipe, i});
                }
            }
        }
        return matrixCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
