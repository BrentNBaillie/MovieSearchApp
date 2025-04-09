package com.example.moviesearchapp.viewmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.moviesearchapp.api.API;
import com.example.moviesearchapp.model.Movie;

import java.util.List;

public class FavouriteViewModel extends ViewModel
{
    private final API api = new API();
    private final MutableLiveData<List<Movie>> favourites = new MutableLiveData<>();

    public LiveData<List<Movie>> getFavorites()
    {
        return favourites;
    }

    public void loadFavorites(String userId)
    {
        api.getFavorites(userId).observeForever(favourites::setValue);
    }
}
