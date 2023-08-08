package com.sample.androidgithubrepositories.ExpandableListView;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.sample.androidgithubrepositories.Database.DBhelper;
import com.sample.androidgithubrepositories.MyApplication;
import com.sample.androidgithubrepositories.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by Adi on 30-06-2017.
 */
public class MultiCheckGenreAdapter extends ExpandableRecyclerViewAdapter<GenreViewHolder, MultiCheckArtistViewHolder> {
    SQLiteDatabase newDb;
    Context mContext;
    public MultiCheckGenreAdapter(List<MultiCheckGenre> groups) {
        super(groups);
    }

   /* @Override
    public MultiCheckArtistViewHolder onCreateCheckChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_multicheck_artist, parent, false);
        return new MultiCheckArtistViewHolder(view);
    }*/

   /* @Override
    public void onBindCheckChildViewHolder(MultiCheckArtistViewHolder holder, int position, CheckedExpandableGroup group, int childIndex) {
        final Artist artist = (Artist) group.getItems().get(childIndex);
        holder.setArtistName(artist.getChild_Repository_Name());
        holder.setDescription(artist.getChild_Description());
        holder.setChild_URL(artist.getChild_URL());
    }*/

    @Override
    public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public MultiCheckArtistViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.list_item_multicheck_artist, parent, false);
        mContext=parent.getContext();
        return new MultiCheckArtistViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(final MultiCheckArtistViewHolder holder, final int flatPosition, ExpandableGroup group, final int childIndex) {
        final Artist artist = (Artist) group.getItems().get(childIndex);
        holder.setArtistName(artist.getChild_Repository_Name());
        Map<Integer,String> kv= MyApplication.integerStringMap();
        Map<String,Integer> getKey= MyApplication.stringIntegerMap();
        if(kv.containsValue(artist.getChild_Description()))
        {
            Integer integer= getKey.get(artist.getChild_Description());
            String heroname = mContext.getString(integer);
            holder.setDescription(heroname);
        }

        holder.setChild_URL(artist.getChild_URL());
        holder.setGroupName(artist.getGroupName());
        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                DBhelper dBhelper = new DBhelper(holder.itemView.getContext());
                newDb = dBhelper.getWritableDatabase();
                newDb.beginTransaction();
                ContentValues cvs = new ContentValues();
                cvs.put("ISBOOKMARK", "YES"); //These Fields should be your String values of actual column names
                newDb.update("AndroidRepositories", cvs, "Name_Of_Repository='" + holder.childCheckedTextView.getText().toString() + "'", null);
                Snackbar snackbar = Snackbar.make(holder.childCheckedTextView.getRootView(), holder.childCheckedTextView.getText().toString() + " "+ mContext.getString (R.string._repository_Bookmarked_), Snackbar.LENGTH_LONG);
                TextView tv = snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snackbar.getView().setBackgroundColor(Color.RED);
                snackbar.show();
                newDb.setTransactionSuccessful();
                newDb.endTransaction();
                likeButton.setLiked(true);
                Expandablelayout.getItemStateArray2().get(holder.getGroupName()).set(childIndex,true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                DBhelper dBhelper = new DBhelper(holder.itemView.getContext());
                newDb = dBhelper.getWritableDatabase();
                newDb.beginTransaction();
                ContentValues cvs = new ContentValues();
                cvs.put("ISBOOKMARK", "NO"); //These Fields should be your String values of actual column names
                newDb.update("AndroidRepositories", cvs, "Name_Of_Repository='" + holder.childCheckedTextView.getText().toString() + "'", null);
                Snackbar snackbar = Snackbar.make(holder.childCheckedTextView.getRootView(), holder.childCheckedTextView.getText().toString() + " "+ mContext.getString (R.string.Removed_from_Bookmarks), Snackbar.LENGTH_LONG);
                TextView tv = snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snackbar.getView().setBackgroundColor(Color.RED);
                snackbar.show();
                newDb.setTransactionSuccessful();
                newDb.endTransaction();
                likeButton.setLiked(false);
                Expandablelayout.getItemStateArray2().get(holder.getGroupName()).set(childIndex,false);
            }
        });

        if (!Expandablelayout.getItemStateArray2().get(group.getTitle()).get(childIndex)) {
            holder.setLikeButton(false);
        }
        else {
            holder.setLikeButton(true);
        }
       /* if (!artist.isFavorite()) {
           holder.setLikeButton(false);
        }
        else {
            holder.setLikeButton(true);
        }*/
    }

    @Override
    public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {
        holder.setGenreTitle(group);
    }
}

