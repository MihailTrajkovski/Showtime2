package com.example.mihail.showtime;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

import java.util.ArrayList;

public class ActorMovieActivity extends AppCompatActivity {
    public boolean contains;
    public ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    public ArrayList<String> getList() {
        return list;
    }
    public String urlVideo = "";
    public HttpRequestsTask task;
    public void setUpAfterAsync()
    {

        String idMovie = getIntent().getStringExtra("Id");

        //favorites

        TextView txt = (TextView)findViewById(R.id.hiddenFavorites);
        String text = txt.getText().toString();
        final Button addToFavorites = (Button) findViewById(R.id.addToFavorites);
        if(text.contains(idMovie))
        {
            addToFavorites.setText("In Favorites");
        }
        else
        {
            addToFavorites.setText("To Favorites");
        }

        //add to watch
        final Button addToToWatch = (Button) findViewById(R.id.addToToWatch);

        TextView txt1 = (TextView)findViewById(R.id.hiddenToWatch);
        String text1 = txt1.getText().toString();
        if(text1.contains(idMovie))
        {
            addToToWatch.setText("In watchlist");
        }
        else
        {
            addToToWatch.setText("To watchlist");
        }
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        TextView txt3 = (TextView) findViewById(R.id.hiddenRated);
        String text3 = txt3.getText().toString();
        contains = text3.contains(idMovie);
        if(text3.contains(idMovie))
        {
            int index = text3.indexOf(idMovie);
            int numIndex = index + idMovie.length();
            int stars = Integer.parseInt(String.valueOf(text3.charAt(numIndex+1)));
            ratingBar.setRating(stars);
        }
        ListView lv = (ListView) findViewById(R.id.actorsList);
        lv.setItemsCanFocus(false);
        lv.clearFocus();
        Intent i = getIntent();
        String id = i.getStringExtra("Id");
        ImageView imgView = (ImageView)findViewById(R.id.imageViewTitle);
        imgView.requestFocus();
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setVerticalScrollbarPosition(position);
        FetchTrailers fetchTrailers = new FetchTrailers(this,id);
        fetchTrailers.execute();

    }
    int position=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_movie);
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.listview_item,list);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        position = scrollView.getVerticalScrollbarPosition();
        ListView lv = (ListView) findViewById(R.id.actorsList);
        lv.setItemsCanFocus(false);
        lv.clearFocus();

         //lv.setEx(true);
        lv.setAdapter(arrayAdapter);

        final Intent i = getIntent();
        final String title = i.getStringExtra("Title");
        TextView tv = (TextView) findViewById(R.id.titleMovie);
        tv.setText(title);
        FetchMovie fmv = new FetchMovie(ActorMovieActivity.this,i.getStringExtra("Id"));
        fmv.execute();
        GetUserTask gut = new GetUserTask(ActorMovieActivity.this,i.getStringExtra("userId"),i.getStringExtra("Id"));
        gut.execute();
        String idMovie = i.getStringExtra("Id");
        final Button addToFavorites = (Button) findViewById(R.id.addToFavorites);
        addToFavorites.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = getIntent();
                String userName = intent.getStringExtra("userId");
                String favorite = intent.getStringExtra("Id");
                if(addToFavorites.getText().toString().equals("Add to Favorites"))
                {
                    addToFavorites.setText("To Favorites");
                    task = new HttpRequestsTask(ActorMovieActivity.this,"https://protected-forest-20580.herokuapp.com/user/"+userName+"/"+favorite,"POST");
                    task.execute();
                }
                else
                {
                    addToFavorites.setText("In Favorites");
                    task = new HttpRequestsTask(ActorMovieActivity.this,"https://protected-forest-20580.herokuapp.com/user/"+userName+"/"+favorite,"DELETE");
                    task.execute();

                }


            }
        });
        final String movieId = getIntent().getStringExtra("Id");
        final Button showTrailers = (Button) findViewById(R.id.videos);
        showTrailers.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Intent i = new Intent(ActorMovieActivity.this,Webview.class);
                //i.putExtra("id",movieId);
                //startActivity(i);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo)));
            }
        });
        final Button addReview = (Button) findViewById(R.id.addReviewButton);
        addReview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EditText et = (EditText)findViewById(R.id.addReview);
                AndroidHttpClient httpClient = new AndroidHttpClient("http://protected-forest-20580.herokuapp.com");
                httpClient.setMaxRetries(5);
                ParameterMap params = httpClient.newParams()
                        .add("username", i.getStringExtra("userId") )
                        .add("movie", i.getStringExtra("Id"))
                        .add("review", et.getText().toString())
                        ;
                httpClient.get("/review", params, new AsyncCallback() {
                    public void onSuccess(HttpResponse httpResponse) {
                        System.out.println(httpResponse.getBodyAsString());
                    }

                    @Override
                    public void onComplete(HttpResponse httpResponse) {
                        System.out.println(httpResponse.getBodyAsString());
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

            }
        });

        final Button addToToWatch = (Button) findViewById(R.id.addToToWatch);
        addToToWatch.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = getIntent();
                String userName = getIntent().getStringExtra("userId");
                String favorite = getIntent().getStringExtra("Id");
                if(addToToWatch.getText().equals("To watchlist"))
                {
                    addToToWatch.setText("In watchlist");
                    task = new HttpRequestsTask(ActorMovieActivity.this,"https://protected-forest-20580.herokuapp.com/user/toWatch/"+userName+"/"+favorite,"POST");
                    task.execute();
                }
                else
                {
                    addToToWatch.setText("To watchlist");
                    task = new HttpRequestsTask(ActorMovieActivity.this,"https://protected-forest-20580.herokuapp.com/user/toWatch/"+userName+"/"+favorite,"DELETE");
                    task.execute();
                }


            }
        });

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String userName = getIntent().getStringExtra("userId");
                String favorite = getIntent().getStringExtra("Id");
                int rat = (int) rating;
                if(contains)
                {
                    HttpRequestsTask task = new HttpRequestsTask(ActorMovieActivity.this,"https://protected-forest-20580.herokuapp.com/user/ratings/"+userName+"/"+favorite+"/"+rat,"PUT");
                    task.execute();
                }
                else
                {
                    HttpRequestsTask task = new HttpRequestsTask(ActorMovieActivity.this,"https://protected-forest-20580.herokuapp.com/user/ratings/"+userName+"/"+favorite+"/"+rat,"POST");
                    task.execute();
                }
            }
        });
        //ratingBar.setNumStars();


    }
}
