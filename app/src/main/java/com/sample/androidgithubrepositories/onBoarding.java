package com.sample.androidgithubrepositories;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.codemybrainsout.onboarder.AhoyOnboarderActivity;
import com.codemybrainsout.onboarder.AhoyOnboarderCard;
import com.sample.androidgithubrepositories.CardView.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class onBoarding extends AhoyOnboarderActivity {
    private static final String PREFERENCES_FILE = "materialsample_settings";
    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    Intent introIntent;
    private boolean isUserFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isUserFirstTime = Boolean.parseBoolean(Utils.readSharedSetting(getApplicationContext(), PREF_USER_FIRST_TIME, "true"));
        introIntent = new Intent(getApplicationContext(), MainActivity.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, false);

        if (!isUserFirstTime)
        {
            startActivity(introIntent);
            finish();
        }

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard(R.string.Onboarding_Title_Browse, R.string.Onboarding_Browse, R.drawable.github_logo);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard(R.string.Onboarding_Title_Search, R.string.Onboarding_Search, R.drawable.search_256);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard(R.string.Onboarding_Title_Bookmark, R.string.Onboarding_Bookmark, R.drawable.bookmark_256);
        //AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard("Notification", "Get Repository of the day as notification!", R.drawable.notification);
        AhoyOnboarderCard ahoyOnboarderCard5 = new AhoyOnboarderCard(R.string.Onboarding_Title_Download, R.string.Onboarding_Download, R.drawable.download);
        AhoyOnboarderCard ahoyOnboarderCard6 = new AhoyOnboarderCard(R.string.Onboarding_Title_Share, R.string.Onboarding_Share, R.drawable.share_256);
        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);
        //ahoyOnboarderCard4.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard5.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard6.setBackgroundColor(R.color.black_transparent);
        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        //pages.add(ahoyOnboarderCard4);
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
        Utils.saveSharedSetting(onBoarding.this, PREF_USER_FIRST_TIME, "false");
        startActivity(introIntent);
    }
}
