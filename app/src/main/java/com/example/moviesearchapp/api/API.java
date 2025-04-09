package com.example.moviesearchapp.api;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.moviesearchapp.model.Movie;
import com.example.moviesearchapp.model.Search;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class API
{
    private static final String url = "https://www.omdbapi.com/";
    private static final String key = "69ccd8bf";
    public MutableLiveData<List<Movie>> movies;
    public MutableLiveData<Movie> movieDetail;
    public MutableLiveData<List<Movie>> favorites;

    interface Route
    {
        @GET("/")
        Call<Search> searchMovies(@Query("apikey") String apiKey, @Query("s") String query);
        @GET("/")
        Call<Movie> getMovie(@Query("apikey") String apiKey, @Query("i") String imdbID);
    }

    private final Route route;
    private final FirebaseFirestore firestore;

    public API()
    {
        route = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build().create(Route.class);
        firestore = FirebaseFirestore.getInstance();
    }

    public LiveData<List<Movie>> search(String query)
    {
        movies = new MutableLiveData<>();
        route.searchMovies(key, query).enqueue(new Callback<Search>()
        {
            @Override
            public void onResponse(Call<Search> call, Response<Search> response)
            {
                if (response.body() != null && response.body().Movies != null)
                {
                    movies.postValue(response.body().Movies);
                }
                else
                {
                    movies.postValue(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t)
            {
                movies.postValue(Collections.emptyList());
            }
        });
        return movies;
    }

    public LiveData<Movie> getMovieDetails(String imdbId)
    {
        movieDetail = new MutableLiveData<>();
        route.getMovie(key, imdbId).enqueue(new Callback<Movie>()
        {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response)
            {
                movieDetail.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t)
            {
                movieDetail.postValue(null);
            }
        });
        return movieDetail;
    }

    public LiveData<List<Movie>> getFavorites(String userId)
    {
        favorites = new MutableLiveData<>();

        firestore.collection("users").document(userId).get()
                .addOnSuccessListener(document ->
                {
                    if (document.exists())
                    {
                        List<Movie> movies = (List<Movie>) document.toObject(UserWrapper.class).movies;
                        favorites.setValue(movies != null ? movies : new ArrayList<>());
                    }
                    else
                    {
                        favorites.setValue(new ArrayList<>());
                    }
                })
                .addOnFailureListener(e ->
                {
                    favorites.setValue(new ArrayList<>());
                });

        return favorites;
    }

    private static class UserWrapper
    {
        public List<Movie> movies;
    }
}
