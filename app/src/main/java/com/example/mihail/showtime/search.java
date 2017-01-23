package com.example.mihail.showtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final List<Movie> list = new ArrayList<>();
        DataAdapter da = new DataAdapter(this, R.id.list_item ,list);
        Intent i = getIntent();
        String query = i.getStringExtra("SearchValue");

        FetchDataTask fetchDataTask = new FetchDataTask(this, list, da, query);
        fetchDataTask.execute();
        final GridView lv = (GridView) findViewById(R.id.list);
        lv.setAdapter(da);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                String title = item.toString();
                Intent intent = getIntent();

                Intent i = new Intent(getApplicationContext(), ActorMovieActivity.class);
                i.putExtra("Id",list.get(position).getId());
                i.putExtra("Title",title);
                String userId = intent.getStringExtra("userId");
                i.putExtra("userId",userId);
                startActivity(i);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}
