package com.sample.androidgithubrepositories.ExpandableListView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.like.LikeButton;
import com.sample.androidgithubrepositories.OpenGithubRepository.open_github_link;
import com.sample.androidgithubrepositories.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;


//public class MultiCheckArtistViewHolder extends CheckableChildViewHolder {
public class MultiCheckArtistViewHolder extends ChildViewHolder {


    TextView childCheckedTextView;
    LikeButton likeButton;
    TextView Description;
    TextView Url;
    String groupName;

    public MultiCheckArtistViewHolder(final View itemView) {
        super(itemView);
        childCheckedTextView = itemView.findViewById(R.id.list_item_multicheck_artist_name);
        Description = itemView.findViewById(R.id.ChildRepositoryDescription);
        Url = itemView.findViewById(R.id.ChildRepositoryURL);
        likeButton = itemView.findViewById(R.id.checkBox2);
        Typeface face = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Gotham.otf");
        childCheckedTextView.setTypeface(face);
        childCheckedTextView.setTextColor(Color.BLACK);
        Description.setTypeface(face);
        Description.setTextColor(Color.BLACK);

        childCheckedTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                navigateToUrl();
                return true;
            }
        });
        Description.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                navigateToUrl();
                return true;
            }
        });


    }

    private void navigateToUrl() {
        Intent intent = new Intent(itemView.getContext(), open_github_link.class);
        Bundle bundle = new Bundle();
        bundle.putString("urltoopen", Url.getText().toString());
        intent.putExtras(bundle);
        itemView.getContext().startActivity(intent);
    }

 /*   @Override
    public Checkable getCheckable() {
        return childCheckedTextView;
    }*/

    public void setArtistName(String artistName) {
        childCheckedTextView.setText(artistName);
    }


    public void setDescription(String description) {
        Description.setText(description);
    }

    public void setChild_URL(String url) {
        Url.setText(url);
    }

    public void setLikeButton(Boolean isLiked) {
        likeButton.setLiked(isLiked);
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
