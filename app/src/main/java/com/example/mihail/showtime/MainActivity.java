package com.example.mihail.showtime;

import android.content.Context;
import android.content.Intent;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private MenuItem mSearchAction;
    public boolean isSearchOpened = false;
    public EditText edtSeach;
    private final static String API_KEY = "5afc1e43624cb397cfaabdc59ad3b782";
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gridView0);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        final RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.gridView1);
        GridLayoutManager gridLayoutManager1= new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(gridLayoutManager1);
        final RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.gridView2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(gridLayoutManager2);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                List<MovieApi> moviesApi = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + moviesApi.size());//TODO: test dali e null broj na filmovi
                recyclerView.setAdapter(new MoviesAdapter(moviesApi, R.layout.list_item, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<MoviesResponse>call, Throwable t) {

                Toast.makeText(getApplicationContext(), "There was a problem try again later", Toast.LENGTH_LONG).show();
                Log.e(TAG, t.toString());
            }
        });
        Call<MoviesResponse> call2 = apiService.getPopularMovies(API_KEY);
        call2.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call2, Response<MoviesResponse> response) {
                int statusCode = response.code();
                List<MovieApi> moviesApi = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + moviesApi.size());
                //android.support.v17.leanback.widget.HorizontalGridView gw = (android.support.v17.leanback.widget.HorizontalGridView) findViewById(R.id.gridView1);

                //MovieApiAdapter mapi = new MovieApiAdapter(getApplicationContext(),R.layout.list_item,moviesApi);
                //gw.setAdapter(new MoviesAdapter(moviesApi, R.layout.list_item, getApplicationContext()));
                recyclerView1.setAdapter(new MoviesAdapter(moviesApi, R.layout.list_item, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<MoviesResponse>call2, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getApplicationContext(), "There was a problem try again later", Toast.LENGTH_LONG).show();
                Log.e(TAG, t.toString());
            }
        });
        Call<MoviesResponse> call3 = apiService.getNowPlaying(API_KEY);
        call3.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call3, Response<MoviesResponse> response) {
                int statusCode = response.code();
                List<MovieApi> moviesApi = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + moviesApi.size());
                //android.support.v17.leanback.widget.HorizontalGridView gw = (android.support.v17.leanback.widget.HorizontalGridView) findViewById(R.id.gridView2);
                recyclerView2.setAdapter(new MoviesAdapter(moviesApi, R.layout.list_item, getApplicationContext()));
                //MovieApiAdapter mapi = new MovieApiAdapter(getApplicationContext(),R.layout.list_item,moviesApi);
                //gw.setAdapter(new MoviesAdapter(moviesApi, R.layout.list_item, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<MoviesResponse>call3, Throwable t) {
                // Log error here since request failed
                Toast.makeText(getApplicationContext(), "There was a problem try again later", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_top50:
                Intent i = new Intent(this,top50.class);
                startActivity(i);
                return true;
            case R.id.action_trailers:
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_open_search));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch(edtSeach.getText().toString(),false);
                        return true;
                    }
                    return false;
                }
            });



            edtSeach.requestFocus();
            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close_search));

            isSearchOpened = true;
        }
    }
    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }
    public void hideInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);
    }
    public void doSearch(String value,boolean whetherTest) {
        if(!whetherTest){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);
        }
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        Intent i = new Intent(this, search.class);
        i.putExtra("userId",userId);
        i.putExtra("SearchValue",value);
        startActivity(i);


    }
}