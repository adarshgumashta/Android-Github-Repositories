package com.sample.androidgithubrepositories.ExpandableListView;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable {

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    private String name;
    private boolean isFavorite;
    private String Child_Repository_Name;
    private String Child_Description;
    private String Child_URL;
    private String GroupName;

    public Artist(String Child_Repository_Name, String Child_Description, String Child_URL, String GroupName) {
        this.Child_Repository_Name = Child_Repository_Name;
        this.Child_Description = Child_Description;
        this.Child_URL = Child_URL;
        this.GroupName = GroupName;
    }

    protected Artist(Parcel in) {
        name = in.readString();
    }


    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getName() {
        return name;
    }

    public String getChild_URL() {
        return Child_URL;
    }

    public void setChild_URL(String child_URL) {
        Child_URL = child_URL;
    }

    public String getChild_Description() {
        return Child_Description;
    }

    public void setChild_Description(String child_Description) {
        Child_Description = child_Description;
    }

    public String getChild_Repository_Name() {
        return Child_Repository_Name;
    }

    public void setChild_Repository_Name(String child_Repository_Name) {
        Child_Repository_Name = child_Repository_Name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artist)) return false;

        Artist artist = (Artist) o;

        /*if (isFavorite() != artist.isFavorite()) return false;*/
        return getName() != null ? getName().equals(artist.getName()) : artist.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        /*result = 31 * result + (isFavorite() ? 1 : 0);*/
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

