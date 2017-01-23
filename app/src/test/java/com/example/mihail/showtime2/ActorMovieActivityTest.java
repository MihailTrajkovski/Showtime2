package com.example.mihail.showtime2;

/**
 * Created by Nikola on 0022 22/01/17 .
 */

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;


import com.example.mihail.showtime.ActorMovieActivity;
import com.example.mihail.showtime.BuildConfig;
import com.example.mihail.showtime.R;
import com.example.mihail.showtime.SignUPActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ActorMovieActivityTest {
    public boolean contains;
    public ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    public ArrayList<String> getList() {
        return list;
    }
    public String urlVideo = "";
    public int position = 0;
    public ScrollView scrollView;
    public ListView lv;
    public Button addToFavorites;
    public ActorMovieActivity activity;
    public Button showTrailers;
    public Button addReview;
    public Button addToToWatch;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(ActorMovieActivity.class);
        arrayAdapter = new ArrayAdapter<String>(activity, R.layout.listview_item,list);
        scrollView = (ScrollView) activity.findViewById(R.id.scrollView);
        position = scrollView.getVerticalScrollbarPosition();
        lv = (ListView) activity.findViewById(R.id.actorsList);
        addToFavorites = (Button) activity.findViewById(R.id.addToFavorites);
        showTrailers = (Button) activity.findViewById(R.id.videos);
        addReview = (Button) activity.findViewById(R.id.addReviewButton);
        addToToWatch = (Button) activity.findViewById(R.id.addToToWatch);
    }

    @Test
    public void checkAddToFavoritesPostTask() {
        addToFavorites.setText("Add to Favorites");
        addToFavorites.performClick();
        String type = activity.task.getType();
        Boolean taskExecuted = activity.task.getHasBeenExecuted();
        assertEquals(type, "POST");
        assertTrue(taskExecuted);
    }

    @Test
    public void checkAddToFavoritesDeleteTask() {
        addToFavorites.setText("Add in Favorites");
        addToFavorites.performClick();
        String type = activity.task.getType();
        Boolean taskExecuted = activity.task.getHasBeenExecuted();
        assertEquals(type, "DELETE");
        assertTrue(taskExecuted);
    }

    @Test
    public void checkShowTrailers() {
        showTrailers.performClick();

        Intent expectedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo));


        ShadowActivity shadowActivity = Shadows.shadowOf(activity);

        Intent actualIntent = shadowActivity.getNextStartedActivity();

        Assert.assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    public void checkAddReview() {
        addReview.performClick();
    }

    /*@Test
    public void checkForNullsInIntent() {
        Intent intent = activity.getIntent();
        String userName = intent.getStringExtra("userId");
        String favorite = intent.getStringExtra("Id");

        assertNotNull(userName);
        assertNotNull(favorite);
    }*/

    @Test
    public void checkAddToWatch_POST() {
        addToToWatch.setText("To watchlist");
        addToToWatch.performClick();



        String type = activity.task.getType();
        Boolean taskExecuted = activity.task.getHasBeenExecuted();
        assertEquals(type, "POST");
        assertTrue(taskExecuted);
    }

    @Test
    public void checkAddToWatch_DELETE() {
        addToToWatch.setText("In watchlist");
        addToToWatch.performClick();



        String type = activity.task.getType();
        Boolean taskExecuted = activity.task.getHasBeenExecuted();
        //assertEquals(type, "POST");
        assertEquals(type, "DELETE");
        assertTrue(taskExecuted);
    }


}

