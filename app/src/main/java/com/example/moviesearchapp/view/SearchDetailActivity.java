package com.example.moviesearchapp.view;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.moviesearchapp.databinding.MovieDetailsBinding;
import com.example.moviesearchapp.model.Movie;
import com.example.moviesearchapp.viewmodel.MovieDetailViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchDetailActivity extends AppCompatActivity
{
    private MovieDetailsBinding binding;
    private MovieDetailViewModel viewModel;
    private String userId;
    private String imdbID;
    private Intent intent;
    boolean exists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = MovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imdbID = getIntent().getStringExtra("imdbID");
        userId = getIntent().getStringExtra("userId");

        viewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);

        viewModel.getMovieDetails(imdbID).observe(this, detail ->
        {

            if (detail != null)
            {
                binding.movieTitle.setText(detail.Title);
                binding.movieYear.setText("Year: " + detail.Year);
                binding.movieRating.setText("Rating: " + detail.Rating);
                binding.studio.setText("Studio: " + (detail.Studio == null ? "N/A" : detail.Studio));
                binding.imdbRating.setText("IMDB Rating: " + (detail.ImdbRating == null ? "N/A" : detail.ImdbRating));
                binding.moviePlot.setText(detail.Plot);
                Glide.with(this).load(detail.Poster).into(binding.moviePoster);

                binding.btnAddToFav.setOnClickListener(v ->
                {
                    if (userId != null)
                    {
                        FirebaseFirestore.getInstance().collection("users").document(userId).get()
                                .addOnSuccessListener(document ->
                                {
                                    if (document.exists())
                                    {
                                        List<Map<String, Object>> movieMaps = (List<Map<String, Object>>) document.get("movies");
                                        List<Movie> movies = new ArrayList<>();

                                        if (movieMaps != null)
                                        {
                                            for (Map<String, Object> map : movieMaps)
                                            {
                                                Movie m = new Movie();
                                                m.ImdbID = (String) map.get("ImdbID");
                                                m.Title = (String) map.get("Title");
                                                m.Year = (String) map.get("Year");
                                                m.Rating = (String) map.get("Rating");
                                                m.Plot = (String) map.get("Plot");
                                                m.Studio = (String) map.get("Production");
                                                m.ImdbRating = (String) map.get("imdbRating");
                                                m.Poster = (String) map.get("Poster");
                                                movies.add(m);
                                            }
                                        }

                                        exists = false;
                                        for (Movie m : movies)
                                        {
                                            if (m.ImdbID.equals(detail.ImdbID))
                                            {
                                                exists = true;
                                                break;
                                            }
                                        }

                                        if (!exists)
                                        {
                                            movies.add(detail);
                                            FirebaseFirestore.getInstance().collection("users").document(userId)
                                                    .update("movies", movies)
                                                    .addOnSuccessListener(unused -> Toast.makeText(this, "Added to Favorites!", Toast.LENGTH_SHORT).show())
                                                    .addOnFailureListener(error -> Toast.makeText(this, "Failed to Add: " + error.getMessage(), Toast.LENGTH_SHORT).show());
                                        }
                                        else
                                        {
                                            Toast.makeText(this, "Movie is already in Favorites!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }
        });

        binding.btnBack.setOnClickListener(v ->
        {
            intent = new Intent(SearchDetailActivity.this, SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }
}
