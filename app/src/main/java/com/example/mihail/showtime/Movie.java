package com.example.mihail.showtime;

/**
 * Created by mihail on 8/14/16.
 */
public class Movie {
    String title;
    String url;
    int year;
    String id;

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    int ranking;
    String rating;
    public Movie(String title, String url, int year, String id)
    {
        this.id = id;
        this.title = title;
        this.url = url;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString()
    {
        if(getRating()!=null)
        {
            return ranking + "." +this.title + "(" + this.year + ")" + "Rating" + getRating() ;
        }
        else
        {
            return this.title;
        }


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
