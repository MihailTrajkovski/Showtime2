package com.example.mihail.showtime2;

import android.os.Build;
import android.widget.ArrayAdapter;

import com.example.mihail.showtime.BuildConfig;
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
public class MoviesAdapterTest {
    private MoviesAdapter moviesAdapter;

    @Before
    public void setUp() throws Exception {
        List<MovieApi> list = new ArrayList<MovieApi>();
        list.add(new MovieApi());
        list.add(new MovieApi());
        list.add(new MovieApi());
        list.add(new MovieApi());
        list.add(new MovieApi());
        MovieApi m = new MovieApi();
        m.setTitle("Gone Girl");
        list.add(m);
        MovieApi m1 = new MovieApi();
        m1.setTitle("Mad Max");
        list.add(m1);
        MovieApi m2 = new MovieApi();
        m2.setTitle("Revenant");
        list.add(m2);

        moviesAdapter = new MoviesAdapter(list,R.layout.list_item,RuntimeEnvironment.application);
    }
    @Test
    public void verifyContext() {
        assertEquals(RuntimeEnvironment.application, moviesAdapter.getContext());
    }
    @Test
    public void verifyListContent() {
        assertEquals(8, moviesAdapter.getItemCount());
        assertEquals(new String("Gone Girl"), moviesAdapter.itemAt(5));
        assertEquals(new String("Mad Max"), moviesAdapter.itemAt(6));
        assertEquals(new String("Revenant"), moviesAdapter.itemAt(7));
        assertNotSame(new String("Gone Baby Gone"),moviesAdapter.itemAt(0));
    }
    @Test
    public void shouldClear() throws Exception {
        moviesAdapter.clear();
        assertEquals(0, moviesAdapter.getItemCount());
    }

}
