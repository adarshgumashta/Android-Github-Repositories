package com.sample.androidgithubrepositories.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class DBAdapter {
    private static final String TAG = "Monkey";
    private DBhelper dBhelper;
    private SQLiteDatabase db;
    private JSONObject jsonObject;
    private String[] TopicNames = new String[32];
    private String[] imageviewTopics = new String[6];
    private String[] Database = new String[3];
    private String[] RecyclerandOtherViewLibraries = new String[10];
    private String[] UIandUX = new String[3];
    private String[] AndroidFullCodes = new String[4];
    private String[] MusicVideoandCamera = new String[5];
    private String[] NetworkLibraries = new String[3];
    private String[] Buttons = new String[2];
    private String[] DateTimeCalenderandotherpickers = new String[5];
    private String[] TextViewandEditText = new String[5];
    private String[] UtilitiesTools = new String[2];
    private String[] Games = new String[2];
    private Context context;

    public DBAdapter(Context context) {
        dBhelper = new DBhelper(context);
        this.context = context;
        db = dBhelper.getWritableDatabase();

        TopicNames[0] = "Materials Design Examples";
        TopicNames[1] = "ImageView";
        TopicNames[2] = "ViewPager";
        TopicNames[3] = "ActionBar";
        TopicNames[4] = "RecyclerandOtherViewLibraries";
        TopicNames[5] = "Animations";
        TopicNames[6] = "Database";
        TopicNames[7] = "UI and UX";
        TopicNames[8] = "Android Full Codes and Code Related";
        TopicNames[9] = "Music and Video and camera";
        TopicNames[10] = "App Intro";
        TopicNames[11] = "Elements";
        TopicNames[12] = "Android System Libraries";
        TopicNames[13] = "Maps";
        TopicNames[14] = "Network Libraries";
        TopicNames[15] = "View";
        TopicNames[16] = "Dialog and Alerts";
        TopicNames[17] = "Status Bar";
        TopicNames[18] = "PulltoRefresh Libraries";
        TopicNames[19] = "SnackBar and Toast";
        TopicNames[20] = "Swipe";
        TopicNames[21] = "Notifications";
        TopicNames[22] = "Navigation Drawer";
        TopicNames[23] = "Buttons";
        TopicNames[24] = "Progress Bar";
        TopicNames[25] = "DateTimeCalender and other pickers";
        TopicNames[26] = "TextView and EditText";
        TopicNames[27] = "Charts Libraries";
        TopicNames[28] = "Search Bar";
        TopicNames[29] = "Utilities Tools";
        TopicNames[30] = "Other Library";
        TopicNames[31] = "Games";

        imageviewTopics[0] = "View";
        imageviewTopics[1] = "Cropping";
        imageviewTopics[2] = "Blur";
        imageviewTopics[3] = "Filter";
        imageviewTopics[4] = "Image Caching Library";
        imageviewTopics[5] = "Others";

        Database[0] = "ORM";
        Database[1] = "SQLite";
        Database[2] = "Android Database";

        RecyclerandOtherViewLibraries[0] = "ListView";
        RecyclerandOtherViewLibraries[1] = "GridView";
        RecyclerandOtherViewLibraries[2] = "WebView";
        RecyclerandOtherViewLibraries[3] = "Spinner";
        RecyclerandOtherViewLibraries[4] = "ScrollView";
        RecyclerandOtherViewLibraries[5] = "Menu";
        RecyclerandOtherViewLibraries[6] = "Parallax";
        RecyclerandOtherViewLibraries[7] = "View Animations";
        RecyclerandOtherViewLibraries[8] = "RecyclerView";
        RecyclerandOtherViewLibraries[9] = "Other things related to ViewGroup";

        UIandUX[0] = "Icons";
        UIandUX[1] = "Bootstrap";
        UIandUX[2] = "Colors";

        AndroidFullCodes[0] = "Examples";
        AndroidFullCodes[1] = "Training";
        AndroidFullCodes[2] = "Tips";
        AndroidFullCodes[3] = "Others";

        MusicVideoandCamera[0] = "Music";
        MusicVideoandCamera[1] = "MusicUI";
        MusicVideoandCamera[2] = "Video";
        MusicVideoandCamera[3] = "Music and Video";
        MusicVideoandCamera[4] = "Camera";

        NetworkLibraries[0] = "HTTP";
        NetworkLibraries[1] = "Volley";
        NetworkLibraries[2] = "Security";

        Buttons[0] = "Floating Action Button";
        Buttons[1] = "Other Buttons";

        DateTimeCalenderandotherpickers[0] = "Date Picker";
        DateTimeCalenderandotherpickers[1] = "Time Picker";
        DateTimeCalenderandotherpickers[2] = "Date and Time Picker";
        DateTimeCalenderandotherpickers[3] = "Calender Picker";
        DateTimeCalenderandotherpickers[4] = "Other Pickers";

        TextViewandEditText[0] = "TextView";
        TextViewandEditText[1] = "EditText";
        TextViewandEditText[2] = "Font and Style";
        TextViewandEditText[3] = "Emoji";
        TextViewandEditText[4] = "Example";

        UtilitiesTools[0] = "Logging and Debugging Tools";
        UtilitiesTools[1] = "Other";

        Games[0] = "Game Engine";
        Games[1] = "Examples";
    }


    public DBAdapter() {

    }

    public void insertData() {

        try {
            jsonObject = new JSONObject(loadJSONFromAsset());
            for (String TopicName : TopicNames) {
                insertRepositoriesinTopics(TopicName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();


    }

    public void insertRepositoriesinTopics(String TopicName) {
        ContentValues cv = new ContentValues();
        JSONArray jsonArray ;
        try {
            jsonArray = jsonObject.getJSONArray(TopicName);
            switch (TopicName) {
                case "ImageView":
                    insertExpandableJSON(jsonArray, TopicName, imageviewTopics);
                    break;
                case "RecyclerandOtherViewLibraries":
                    insertExpandableJSON(jsonArray, TopicName, RecyclerandOtherViewLibraries);
                    break;
                case "Database":
                    insertExpandableJSON(jsonArray, TopicName, Database);
                    break;
                case "UI and UX":
                    insertExpandableJSON(jsonArray, TopicName, UIandUX);
                    break;
                case "Android Full Codes and Code Related":
                    insertExpandableJSON(jsonArray, TopicName, AndroidFullCodes);
                    break;
                case "Music and Video and camera":
                    insertExpandableJSON(jsonArray, TopicName, MusicVideoandCamera);
                    break;
                case "Network Libraries":
                    insertExpandableJSON(jsonArray, TopicName, NetworkLibraries);
                    break;
                case "DateTimeCalender and other pickers":
                    insertExpandableJSON(jsonArray, TopicName, DateTimeCalenderandotherpickers);
                    break;
                case "TextView and EditText":
                    insertExpandableJSON(jsonArray, TopicName, TextViewandEditText);
                    break;
                case "Utilities Tools":
                    insertExpandableJSON(jsonArray, TopicName, UtilitiesTools);
                    break;
                case "Games":
                    insertExpandableJSON(jsonArray, TopicName, Games);
                    break;
                case "Buttons":
                    insertExpandableJSON(jsonArray, TopicName, Buttons);
                    break;
                default:
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                        cv.put(DBhelper.Topic_Name, TopicName);
                        cv.put(DBhelper.Sub_Topic_Name, "");
                        String input = jsonObject1.get("Name_Of_Repository").toString();
                        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
                        cv.put(DBhelper.Name_Of_Repository, output);
                        cv.put(DBhelper.Description, jsonObject1.get("Description").toString());
                        cv.put(DBhelper.URL, jsonObject1.get("URL").toString());
                        db.beginTransaction();
                        long id = db.insert(DBhelper.TABLE_NAME, null, cv);
                        // Log.d(TAG,"Inserting Data");
                        db.setTransactionSuccessful();
                        db.endTransaction();
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void insertExpandableJSON(JSONArray jsonArray, String topicName, String[] Topicsarray) {
        ContentValues cv1 = new ContentValues();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject1 ;
            try {
                Log.d(TAG, topicName);
                jsonObject1 = (JSONObject) jsonArray.get(i);
                for (String aTopicsarray : Topicsarray) {
                    JSONArray jsonArray1 = jsonObject1.getJSONArray(aTopicsarray);
                    for (int k = 0; k < jsonArray1.length(); k++) {
                        JSONObject jsonObject2 = (JSONObject) jsonArray1.get(k);
                        String input = jsonObject2.get("Name_Of_Repository").toString();
                        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
                        cv1.put(DBhelper.Topic_Name, topicName);
                        cv1.put(DBhelper.Sub_Topic_Name, aTopicsarray);
                        cv1.put(DBhelper.Name_Of_Repository, output);
                        cv1.put(DBhelper.Description, jsonObject2.get("Description").toString());
                        cv1.put(DBhelper.URL, jsonObject2.get("URL").toString());
                        db.beginTransaction();
                        long id = db.insert(DBhelper.TABLE_NAME, null, cv1);
                        //Log.d(TAG, "Inserting Data");
                        //Toast.makeText(context, String.valueOf(id), Toast.LENGTH_SHORT).show();
                        db.setTransactionSuccessful();
                        db.endTransaction();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String loadJSONFromAsset() {
        String json ;
        try {
            InputStream is = this.context.getAssets().open("fonts/GithubRepositoriesArray.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            return json;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }
}