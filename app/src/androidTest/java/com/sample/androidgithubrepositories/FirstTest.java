package com.sample.androidgithubrepositories;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.sample.androidgithubrepositories.CardView.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.RecyclerViewActions.scrollToPosition;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FirstTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public FirstTest() {
        super(MainActivity.class);
    }

   /* @Test
    public void test1ChatId() {
        getActivity();
        onView(withText("Hello World!")).check(matches(isDisplayed()));
    }
*/
   public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
       return new RecyclerViewMatcher(recyclerViewId);
   }

    public static void tapRecyclerViewItem(int recyclerViewId, int position) {
        onView(withId(recyclerViewId)).perform(scrollToPosition(position));
        onView(withRecyclerView(recyclerViewId).atPosition(position)).perform(click());
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

   @Test
   public void testScrollingAt()throws Exception
   {
       getActivity();
       // Scrolls till the position 20

       // Click item at position 3
       onView(withRecyclerView(R.id.recycler_view).atPosition(0)).perform(click());
   }
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
}