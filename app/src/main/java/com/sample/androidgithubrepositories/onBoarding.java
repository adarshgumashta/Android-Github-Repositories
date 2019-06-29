package com.sample.androidgithubrepositories;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.sample.androidgithubrepositories.CardView.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class onBoarding extends AhoyOnboarderActivity {
    private static final String PREFERENCES_FILE = "materialsample_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Browse", "Browse between 500+ repositories divided into various categories!", R.drawable.github_logo);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Search", "Search for a particular category!", R.drawable.search_256);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Bookmark", "Bookmark and view your favourite repostories!", R.drawable.bookmark_256);
        AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard("Notification", "Get Repository of the day as notification!", R.drawable.notification);
        AhoyOnboarderCard ahoyOnboarderCard5 = new AhoyOnboarderCard("Download", "Download and unzip to see files of repositories!", R.drawable.download);
        AhoyOnboarderCard ahoyOnboarderCard6 = new AhoyOnboarderCard("Share", "Liked any repository?Just Share them with your colleagues/friends!", R.drawable.share_256);
        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard4.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard5.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard6.setBackgroundColor(R.color.black_transparent);
        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        pages.add(ahoyOnboarderCard4);
        pages.add(ahoyOnboarderCard5);
        pages.add(ahoyOnboarderCard6);
        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
            //page.setTitleTextSize(dpToPixels(12, this));
            //page.setDescriptionTextSize(dpToPixels(8, this));
            //page.setIconLayoutParams(width, height, marginTop, marginLeft, marginRight, marginBottom);
        }

        setFinishButtonTitle("Finish");
        showNavigationControls(true);
        setGradientBackground();

        //set the button style you created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));
        }

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        setFont(face);

        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        finish();
        //  update 1st time pref
        Utils.saveSharedSetting(onBoarding.this, MainActivity.PREF_USER_FIRST_TIME, "false");
    }
}
