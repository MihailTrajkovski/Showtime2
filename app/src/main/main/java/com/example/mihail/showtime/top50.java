package com.example.mihail.showtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class top50 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top50);
        List<Movie> list = new ArrayList<>();
        DataAdapter da = new DataAdapter(this,R.id.list_item ,list);
        FetchDataTask fetchDataTask = new FetchDataTask(this, list, da);
        fetchDataTask.execute();
        GridView lv = (GridView) findViewById(R.id.gridViewTop50);
        lv.setAdapter(da);


    }
}
