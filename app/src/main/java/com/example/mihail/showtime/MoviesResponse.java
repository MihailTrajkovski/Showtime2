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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieApi> getResults() {
        return results;
    }

    public void setResults(List<MovieApi> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
