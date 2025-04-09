package com.example.moviesearchapp.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesearchapp.model.Movie;
import com.example.moviesearchapp.api.API;

public class MovieDetailViewModel extends ViewModel
{
    private final API api = new API();
    public LiveData<Movie> getMovieDetails(String imdbID)
    {
        return api.getMovieDetails(imdbID);
    }
}
