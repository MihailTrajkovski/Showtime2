package com.example.mihail.showtime;

/**
 * Created by mihail on 11.9.16.
 */
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MoviesResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<MovieApi> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;


    public List<MovieApi> getResults() {
        return results;
    }

}
