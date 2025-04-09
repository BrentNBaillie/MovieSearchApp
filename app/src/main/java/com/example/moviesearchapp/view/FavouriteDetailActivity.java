package com.example.moviesearchapp.view;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.moviesearchapp.R;
import com.example.moviesearchapp.model.Movie;
import com.example.moviesearchapp.viewmodel.FavouriteDetailViewModel;

public class FavouriteDetailActivity extends AppCompatActivity
{
    private FavouriteDetailViewModel viewModel;
    private Movie movie;
    private String userId;
    private TextView titleView;
    private TextView yearView;
    private TextView ratingView;
    private EditText plotEdit;
    private ImageView posterView;
    private Intent intent;
    private String updatedPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_details);

        titleView = findViewById(R.id.movieTitle);
        yearView = findViewById(R.id.movieYear);
        ratingView = findViewById(R.id.movieRating);
        plotEdit = findViewById(R.id.moviePlot);
        posterView = findViewById(R.id.moviePoster);

        viewModel = new ViewModelProvider(this).get(FavouriteDetailViewModel.class);
        movie = (Movie) getIntent().getSerializableExtra("movie");
        userId = getIntent().getStringExtra("userId");

        if (movie != null)
        {
            titleView.setText(movie.Title);
            yearView.setText("Year: " + movie.Year);
            ratingView.setText("Rating: " + movie.Rating);
            plotEdit.setText(movie.Plot);
            Glide.with(this).load(movie.Poster).into(posterView);
        }

        findViewById(R.id.btnBack).setOnClickListener(v ->
        {
            intent = new Intent(FavouriteDetailActivity.this, FavouriteActivity.class);
            intent.putExtra("userId", userId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        findViewById(R.id.btnUpdate).setOnClickListener(v ->
        {
            if (movie != null && userId != null)
            {
                updatedPlot = plotEdit.getText().toString().trim();
                movie.Plot = updatedPlot;
                viewModel.updateMoviePlot(userId, movie.ImdbID, movie.Plot);
                Toast.makeText(this, "Movie updated", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnDelete).setOnClickListener(v ->
        {
            if (movie != null && userId != null)
            {
                viewModel.deleteMovie(userId, movie.ImdbID);
                Toast.makeText(this, "Movie deleted", Toast.LENGTH_SHORT).show();
                intent = new Intent(FavouriteDetailActivity.this, FavouriteActivity.class);
                intent.putExtra("userId", userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
