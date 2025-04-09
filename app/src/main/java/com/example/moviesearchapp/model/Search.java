package com.example.moviesearchapp.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Search
{
    @SerializedName("Search")
    public List<Movie> Movies;
}
