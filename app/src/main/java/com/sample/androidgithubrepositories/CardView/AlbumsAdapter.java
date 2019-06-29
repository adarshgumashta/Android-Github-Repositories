package com.sample.androidgithubrepositories.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sample.androidgithubrepositories.CustomListView.SecondActivity;
import com.sample.androidgithubrepositories.R;
import com.sample.androidgithubrepositories.Receiver.PrefManager;

import java.util.List;

/**
 * Created by Adi on 27-11-2016.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {
    Intent intent;
    Bundle bundle;
    InterstitialAd mInterstitialAd;
    PrefManager pref;
    private Context mContext;
    private List<Album> albumList;

    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    public AlbumsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(album.getNumOfSongs() + " repositories");
        holder.versionName = album.getName();
        mInterstitialAd = new InterstitialAd(mContext);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(mContext.getString(R.string.interstitial_full_screen));
        // Load ads into Interstitial Ads

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
        //loading album cover using glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(view, holder);
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(view, holder);
            }
        });
        holder.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate(view, holder);
            }
        });

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void navigate(View view, MyViewHolder holder) {
        pref = new PrefManager(mContext);
        final AdRequest adRequest = new AdRequest.Builder()
                .build();
        if ((pref.getShowaddvar() % 3 == 0)) {
            mInterstitialAd.loadAd(adRequest);
        }
        pref.setShowaddvar(pref.getShowaddvar() + 1);
        intent = new Intent(view.getContext(), SecondActivity.class);
        bundle = new Bundle();
        bundle.putString("position", String.valueOf(holder.getAdapterPosition()));
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail;
        public String versionName;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            count = view.findViewById(R.id.count);
            thumbnail = view.findViewById(R.id.thumbnail);
            Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/Gotham.otf");
            count.setTypeface(face);
            title.setTypeface(face);
            count.setTextColor(Color.BLACK);
            title.setTextColor(Color.BLACK);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(v.getContext(), SecondActivity.class);
                    bundle = new Bundle();
                    bundle.putString("position", String.valueOf(getAdapterPosition()));
                    intent.putExtras(bundle);
                    v.getContext().startActivity(intent);

                }
            });

        }
    }
}
