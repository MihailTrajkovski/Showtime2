package com.example.mihail.showtime2;

import android.os.Build;
import android.widget.ArrayAdapter;

import com.example.mihail.showtime.BuildConfig;
import com.example.mihail.showtime.DataAdapter;
import com.example.mihail.showtime.Movie;
import com.example.mihail.showtime.MovieApi;
import com.example.mihail.showtime.MoviesAdapter;
import com.example.mihail.showtime.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

/**
 * Created by mihail on 22.1.17.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP )
@RunWith(RobolectricTestRunner.class)
public class DataAdapterTest {
    private DataAdapter dataAdapter;

    @Before
    public void setUp() throws Exception {
        List<Movie> list = new ArrayList<Movie>();
        list.add(new Movie("Movie1","",2004,"tt0044541"));
        list.add(new Movie("Movie2","",2014,"tt0044542"));
        list.add(new Movie("Movie3","",1994,"tt0044543"));
        list.add(new Movie("Movie4","",2015,"tt0044544"));
        list.add(new Movie("Movie5","",2000,"tt0044545"));


        dataAdapter = new DataAdapter(RuntimeEnvironment.application,R.layout.list_item,list);
    }
    //проверува дали контектстот во кој е повикан овој адаптер е ист со контекстот
    @Test
    public void verifyContext() {
        assertEquals(RuntimeEnvironment.application, dataAdapter.getContext());
    }
    //ги проверува вредностите кои моментално се наоѓаат во адаптерот дали се еднакви со посакуваните вредности
    @Test
    public void verifyListContent() {
        assertEquals(5, dataAdapter.getCount());
        assertEquals(new String("Movie1"), dataAdapter.getItem(0).getTitle());
        assertEquals(2000, dataAdapter.getItem(4).getYear());
        assertEquals(new String("tt0044543"), dataAdapter.getItem(2).getId());

    }
    //Проверува дали функцијата clear() работи како што треба кај адаптерите
    @Test
    public void shouldClear() throws Exception {
        dataAdapter.clear();
        assertEquals(0, dataAdapter.getCount());
    }

}
