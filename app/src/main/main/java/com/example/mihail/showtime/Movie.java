package com.example.mihail.showtime;

/**
 * Created by mihail on 8/14/16.
 */
public class Movie {
    String title;
    String url;
    int year;
    Movie(String title, String url, int year)
    {
        this.title = title;
        this.url = url;
        this.year = year;
    }
    @Override
    public String toString()
    {
        return this.title + "(" + this.year + ")";
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {

        return title;
    }

    public int getYear() {

        return year;
    }
}
