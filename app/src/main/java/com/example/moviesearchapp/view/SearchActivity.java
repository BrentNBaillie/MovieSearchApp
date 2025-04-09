package com.example.moviesearchapp.view;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.moviesearchapp.adapter.Adapter;
import com.example.moviesearchapp.api.API;
import com.example.moviesearchapp.databinding.SearchBinding;
import com.example.moviesearchapp.model.Movie;
import com.example.moviesearchapp.viewmodel.MovieViewModel;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity
{
    private Adapter adapter;
    private SearchBinding mainBinding;
    private MovieViewModel movieViewModel;
    private String userId;
    private API api;
    private Movie movie;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainBinding = SearchBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        userId = getIntent().getStringExtra("userId");

        setupRecyclerView();
        setupViewModel();
        setupSearchButton();
        setupFavoritesButton();
    }

    private void setupRecyclerView()
    {
        api = new API();
        adapter = new Adapter(new ArrayList<>(), (view, position) ->
        {
            movie = adapter.movies.get(position);
            intent = new Intent(this, SearchDetailActivity.class);
            intent.putExtra("imdbID", movie.ImdbID);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }, api);
        mainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.recyclerView.setAdapter(adapter);
    }

    private void setupViewModel()
    {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.movies.observe(this, movies ->
        {
            if (movies != null && !movies.isEmpty())
            {
                adapter.updateList(movies);
            }
        });
    }

    private void setupSearchButton()
    {
        mainBinding.searchButton.setOnClickListener(b ->
        {
            String query = mainBinding.searchEdit.getText().toString().trim();
            if (query.isEmpty())
            {
                mainBinding.searchEdit.setError("Please enter a movie title");
            }
            else
            {
                movieViewModel.setQuery(query);
            }
        });
    }

    private void setupFavoritesButton()
    {
        mainBinding.btnFavorites.setOnClickListener(v ->
        {
            intent = new Intent(this, FavouriteActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }
}
