package com.example.mihail.showtime2;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.widget.EditText;

import com.example.mihail.showtime.BuildConfig;
import com.example.mihail.showtime.MainActivity;
import com.example.mihail.showtime.R;
import com.example.mihail.showtime.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP )
@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(MainActivity.class);
    }
    //проверува дали полето за пребарување постои
    @Test
    public void isSearchBarNullAtBeginning()throws Exception{

        EditText et = (EditText) activity.findViewById(R.id.edtSearch);
        assertNull("EditText could not be found", et);
    }
    public void testIsSearchBarClicked() throws Exception{
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        shadowActivity.clickMenuItem(R.id.action_search);
        EditText et = (EditText) activity.findViewById(R.id.edtSearch);
        assertNotNull(et);
    }
    @Test
    public void isSearchOpened() throws Exception{
        activity.handleMenuSearch();
        assertEquals(true,activity.isSearchOpened);
        activity.handleMenuSearch();
        assertEquals(false,activity.isSearchOpened);
    }
    //проверува дали може да се кликне на некое мени поле во овој случај за копчето
    @Test
    public void searchClicked() throws Exception{
        //activity.findViewById(R.id.edtSearch).performClick();
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        shadowActivity.clickMenuItem(R.id.action_search);
        assertEquals(true,activity.isSearchOpened);
    }
    //проверува дали може да се кликне на полето за пребаруваље
    @Test
    public void isSearchStarted() throws Exception
    {
        activity.doSearch("Mad Max",true);
        activity.edtSeach = (EditText) activity.findViewById(R.id.edtSearch);
        Intent expectedIntent = new Intent(activity, search.class);

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent actualIntent = shadowActivity.getNextStartedActivity();

        assertEquals(actualIntent.getStringExtra("SearchValue"), "Mad Max");

    }


}