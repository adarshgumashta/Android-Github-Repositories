package com.sample.androidgithubrepositories.CustomListView;

/**
 * Created by Adi on 11-12-2016.
 */
public class ListCollection {

    String Repository_Name;
    String Description;

    String Topic_Name;
    String URL;
    boolean isChecked;

    public ListCollection() {
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTopic_Name() {
        return Topic_Name;
    }

    public void setTopic_Name(String topic_Name) {
        Topic_Name = topic_Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRepository_Name() {
        return Repository_Name;
    }

    public void setRepository_Name(String repository_Name) {
        Repository_Name = repository_Name;
    }

}
