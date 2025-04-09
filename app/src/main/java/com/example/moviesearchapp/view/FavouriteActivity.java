package com.example.moviesearchapp.view;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.moviesearchapp.adapter.Adapter;
import com.example.moviesearchapp.api.API;
import com.example.moviesearchapp.databinding.FavouriteBinding;
import com.example.moviesearchapp.model.Movie;
import com.example.moviesearchapp.viewmodel.FavouriteViewModel;
import com.example.moviesearchapp.viewmodel.SharedMovieViewModel;
import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity
{
    private FavouriteViewModel viewModel;
    private SharedMovieViewModel sharedMovieViewModel;
    private Adapter adapter;
    private FavouriteBinding binding;
    private String userId;
    private Intent intent;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = FavouriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getStringExtra("userId");

        sharedMovieViewModel = new ViewModelProvider(this).get(SharedMovieViewModel.class);

        adapter = new Adapter(new ArrayList<>(), (view, position) ->
        {
            movie = adapter.movies.get(position);
            intent = new Intent(FavouriteActivity.this, FavouriteDetailActivity.class);
            intent.putExtra("movie", movie);
            intent.putExtra("userId", userId);
            startActivity(intent);
        }, new API());

        binding.favRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.favRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        if (userId != null)
        {
            viewModel.loadFavorites(userId);
            viewModel.getFavorites().observe(this, movies ->
            {
                adapter.updateList(movies);
                sharedMovieViewModel.setFavourites(movies);
            });
        }

        binding.btnSearch.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (userId != null)
        {
            viewModel.loadFavorites(userId);
            viewModel.getFavorites().observe(this, movies ->
            {
                adapter.updateList(movies);
                sharedMovieViewModel.setFavourites(movies);
            });
        }
    }
}
