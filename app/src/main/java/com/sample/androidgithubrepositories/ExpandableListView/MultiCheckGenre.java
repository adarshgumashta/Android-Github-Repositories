package com.sample.androidgithubrepositories.ExpandableListView;

import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup;

import java.util.List;

public class MultiCheckGenre extends MultiCheckExpandableGroup {

    private int iconResId;

    public MultiCheckGenre(String title, List<Artist> items, int iconResId) {
        super(title, items);
        this.iconResId = iconResId;
    }

    public MultiCheckGenre(String title, List<Artist> items) {
        super(title, items);
    }

    public int getIconResId() {
        return iconResId;
    }
}

