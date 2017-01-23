package com.example.mihail.showtime2;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mihail.showtime.ActorMovieActivity;
import com.example.mihail.showtime.BuildConfig;
import com.example.mihail.showtime.DataAdapter;
import com.example.mihail.showtime.FetchDataTask;
import com.example.mihail.showtime.FetchMovie;
import com.example.mihail.showtime.FetchTrailers;
import com.example.mihail.showtime.GetUserTask;
import com.example.mihail.showtime.HttpRequestsTask;
import com.example.mihail.showtime.MainActivity;
import com.example.mihail.showtime.Movie;
import com.example.mihail.showtime.R;
import com.example.mihail.showtime.search;
import com.example.mihail.showtime.top50;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowActivity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP )
@RunWith(RobolectricTestRunner.class)
public class AsyncTaskTests
{
    private top50 activty;
    private ActorMovieActivity actorMovieActivity;
    private List<Movie> top50list;
    @Before
    public void setup(){
        activty = Robolectric.setupActivity(top50.class);
        actorMovieActivity = Robolectric.setupActivity(ActorMovieActivity.class);

    }

    @Test
    public void testUserTask() throws Exception{
        Intent i = new Intent();
        i.putExtra("Id","tt1392190");
        i.putExtra("Title","Mad Max");
        String userId = i.getStringExtra("d");
        i.putExtra("userId",userId);

        actorMovieActivity.setIntent(i);
        GetUserTask gut = new GetUserTask(actorMovieActivity,"d","tt1392190");
        gut.execute();
        TextView txt1 = (TextView)actorMovieActivity.findViewById(R.id.hiddenRated);
        TextView txt2 = (TextView)actorMovieActivity.findViewById(R.id.hiddenToWatch);
        TextView txt3 = (TextView)actorMovieActivity.findViewById(R.id.hiddenFavorites);
        assertNotSame(txt1.getText().toString(),"");
        assertNotSame(txt2.getText().toString(),"");
        assertNotSame(txt3.getText().toString(),"");

    }
    @Test
    public void testTrailerLinkSet() throws Exception{
        FetchTrailers fetchTrailers = new FetchTrailers(actorMovieActivity,"tt1392190");
        fetchTrailers.execute();
        boolean test = actorMovieActivity.urlVideo.contains("youtube");
        assertTrue(test);
    }
    @Test
    public void testFetchMovie() throws Exception{
        FetchMovie fmv = new FetchMovie(actorMovieActivity,"tt1392190");
        fmv.execute();
        TextView txt = (TextView)actorMovieActivity.findViewById(R.id.descriptionText);
        String description = txt.getText().toString();
        assertNotSame(description,"");
    }
    @Test
    public void testRequest() throws Exception{
        HttpRequestsTask task = new HttpRequestsTask(actorMovieActivity,"https://protected-forest-20580.herokuapp.com/user/"+"d"+"/"+"tt1392190","POST");
        task.execute();
        assertTrue(task.getHasBeenExecuted());
    }
    @Test
    public void testTopFiftyEmpty()throws Exception{
        assertNotNull("Object is null",activty.list);
        assertNotSame(0,activty.list.size());

    }
    @Test
    public void testWhetherrAllFifty()throws Exception{
        assertEquals(50,activty.list.size());
    }



}
