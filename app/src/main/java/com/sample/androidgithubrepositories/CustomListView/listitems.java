package com.sample.androidgithubrepositories.CustomListView;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.sample.androidgithubrepositories.Database.DBhelper;
import com.sample.androidgithubrepositories.R;

import java.util.ArrayList;

public class listitems extends ArrayAdapter<ListCollection> {

    private final Context context;
    private final ArrayList values;
    SQLiteDatabase newDb;
    private boolean[] checkBoxState;
    public listitems(Context context, ArrayList values) {
        super(context, R.layout.activity_listitems, values);
        this.context = context;
        this.values = values;
        checkBoxState = new boolean[values.size()];
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.activity_listitems, parent, false);
        holder.repositoryName = rowView.findViewById(R.id.textView2);
        holder.repositoryDesc  = rowView.findViewById(R.id.textView3);
        holder.bookmark = rowView.findViewById(R.id.checkBox2);
        holder.repositoryDesc.setTextColor(Color.BLACK);
        holder.repositoryName.setTextColor(Color.BLACK);

        rowView.setTag(holder);
        final ListCollection lc = (ListCollection) values.get(position);
        /*holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                if (cb.isChecked()) {
                    lc.setChecked(true);
                    checkBoxState[position] = true;
                } else {
                    lc.setChecked(false);
                    checkBoxState[position] = false;
                }

            }
        });
        if (values.get(position) != null) {
            holder.name.setChecked(checkBoxState[position]);
        }*/
        if (values.get(position) != null) {
            holder.bookmark.setLiked(lc.isChecked());
        }
        holder.bookmark.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                DBhelper dBhelper = new DBhelper(context);
                newDb = dBhelper.getWritableDatabase();
                newDb.beginTransaction();
                ContentValues cvs = new ContentValues();
                cvs.put("ISBOOKMARK", "YES"); //These Fields should be your String values of actual column names
                newDb.update("AndroidRepositories", cvs, "Name_Of_Repository='" + holder.repositoryName.getText().toString() + "'", null);
                Snackbar snackbar = Snackbar.make(holder.repositoryName.getRootView(), holder.repositoryName.getText().toString() +" "+ context.getString (R.string._repository_Bookmarked_), Snackbar.LENGTH_LONG);
                TextView tv = snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snackbar.getView().setBackgroundColor(Color.RED);
                snackbar.show();
                newDb.setTransactionSuccessful();
                newDb.endTransaction();
                lc.setChecked(true);
                holder.bookmark.setLiked(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                DBhelper dBhelper = new DBhelper(context);
                newDb = dBhelper.getWritableDatabase();
                newDb.beginTransaction();
                ContentValues cvs = new ContentValues();
                cvs.put("ISBOOKMARK", "NO"); //These Fields should be your String values of actual column names
                newDb.update("AndroidRepositories", cvs, "Name_Of_Repository='" + holder.repositoryName.getText().toString() + "'", null);
                Snackbar snackbar = Snackbar.make(holder.repositoryName.getRootView(), holder.repositoryName.getText().toString() +" "+context.getString(R.string.Removed_from_Bookmarks), Snackbar.LENGTH_LONG);
                TextView tv = snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snackbar.getView().setBackgroundColor(Color.RED);
                snackbar.show();
                newDb.setTransactionSuccessful();
                newDb.endTransaction();
                lc.setChecked(false);
                holder.bookmark.setLiked(false);
            }
        });
        holder.repositoryName.setText(lc.getRepository_Name());
        holder.repositoryDesc.setText(lc.getDescription());
        return rowView;
    }

    private class ViewHolder {
        LikeButton bookmark;
        TextView repositoryName,repositoryDesc;
    }


}
