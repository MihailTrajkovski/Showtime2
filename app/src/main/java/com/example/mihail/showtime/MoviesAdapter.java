package com.example.mihail.showtime;

/**
 * Created by mihail on 11.9.16.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;



public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<MovieApi> movies;
    private int rowLayout;
    private Context context;

    public Context getContext() {
        return context;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        FrameLayout moviesLayout;
        TextView movieTitle;
        ImageView image;


        public MovieViewHolder(View v) {
            super(v);
            moviesLayout = (FrameLayout) v.findViewById(R.id.layoutMovies);
            movieTitle = (TextView) v.findViewById(R.id.title);
            image = (ImageView) v.findViewById(R.id.image);

        }
    }

    public MoviesAdapter(List<MovieApi> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MovieViewHolder(view);
    }

    public void clear(){
        this.movies.clear();
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.movieTitle.setText(movies.get(position).getTitle());
        String url = "http://image.tmdb.org/t/p/w300" + movies.get(position).getBackdropPath();
        Picasso
                .with(context)
                .load(url)
                .fit() // will explain later
                .centerInside()
                .placeholder(R.drawable.ic_open_search)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    public String  itemAt(int index)
    {
        return movies.get(index).getTitle();
    }
}