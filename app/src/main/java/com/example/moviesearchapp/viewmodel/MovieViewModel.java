package com.example.moviesearchapp.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.example.moviesearchapp.model.Movie;
import com.example.moviesearchapp.api.API;
import java.util.List;

public class MovieViewModel extends ViewModel
{
    private final MutableLiveData<String> query = new MutableLiveData<>();
    private final API api = new API();
    public final LiveData<List<Movie>> movies = Transformations.switchMap(query, api::search);

    public void setQuery(String queryString)
    {
        query.setValue(queryString);
    }
}
