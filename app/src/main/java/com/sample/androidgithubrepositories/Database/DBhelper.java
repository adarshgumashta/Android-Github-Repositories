package com.sample.androidgithubrepositories.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Adi on 13-07-2017.
 */
public class DBhelper extends SQLiteOpenHelper {
    // Table Name
    public static final String TABLE_NAME = "AndroidRepositories";
    // Table columns
    public static final String _ID = "_id";
    public static final String Topic_Name = "Topic_Name";
    public static final String Sub_Topic_Name = "Sub_Topic_Name";
    public static final String Name_Of_Repository = "Name_Of_Repository";
    public static final String Description = "Description";
    public static final String URL = "URL";
    public static final String ISBOOKMARK = "ISBOOKMARK";
    // Database Information
    static final String DB_NAME = "GitHubRepositories.DB";
    private static final int DATABASE_Version = 1;
    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Topic_Name + " TEXT NOT NULL, " + Sub_Topic_Name + " TEXT NOT NULL, " + Name_Of_Repository + " TEXT NOT NULL, " + Description + " TEXT NOT NULL, " + URL + " TEXT NOT NULL, " + ISBOOKMARK + " TEXT );";
    private Context context;

    public DBhelper(Context context) {
        super(context, DB_NAME, null, DATABASE_Version);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public ArrayList<String> getAllName() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> employeeNames = new ArrayList<String>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String name = cursor.getString(cursor.getColumnIndex(Name_Of_Repository));
                employeeNames.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return employeeNames;

    }

    public Cursor getAllEmployeesDescription(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selections = {id};
        String columns[] = {Name_Of_Repository, URL};
        Cursor cursor = db.query(TABLE_NAME, columns, Name_Of_Repository + "=?", selections, null, null, null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        Toast.makeText(context, "Drop", Toast.LENGTH_SHORT).show();
        onCreate(db);
    }
}
