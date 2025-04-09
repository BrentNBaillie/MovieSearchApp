package com.example.moviesearchapp.viewmodel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.moviesearchapp.model.Movie;
import java.util.ArrayList;
import java.util.List;

public class SharedMovieViewModel extends ViewModel
{
    private final MutableLiveData<List<Movie>> favouriteMovies = new MutableLiveData<>(new ArrayList<>());

    public void setFavourites(List<Movie> list)
    {
        favouriteMovies.setValue(list);
    }
}
