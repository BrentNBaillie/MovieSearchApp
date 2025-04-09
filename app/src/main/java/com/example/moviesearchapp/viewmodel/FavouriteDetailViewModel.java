package com.example.moviesearchapp.viewmodel;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavouriteDetailViewModel extends ViewModel
{
    private final FirebaseFirestore firebase = FirebaseFirestore.getInstance();
    private List<Map<String, Object>> movies;
    private List<Map<String, Object>> updated;

    public void updateMoviePlot(String userId, String imdbID, String updatedPlot)
    {
        firebase.collection("users").document(userId).get().addOnSuccessListener(document ->
        {
            if (document.exists())
            {
                movies = (List<Map<String, Object>>) document.get("movies");
                if (movies == null) return;

                for (Map<String, Object> movie : movies)
                {
                    if (imdbID.equals(movie.get("ImdbID")))
                    {
                        movie.put("Plot", updatedPlot);
                        break;
                    }
                }

                firebase.collection("users").document(userId).update("movies", movies);
            }
        });
    }

    public void deleteMovie(String userId, String imdbID)
    {
        firebase.collection("users").document(userId).get().addOnSuccessListener(document ->
        {
            if (document.exists()) {
                movies = (List<Map<String, Object>>) document.get("movies");
                if (movies == null) return;

                updated = new ArrayList<>();
                for (Map<String, Object> movie : movies)
                {
                    if (!imdbID.equals(movie.get("ImdbID")))
                    {
                        updated.add(movie);
                    }
                }

                firebase.collection("users").document(userId).update("movies", updated);
            }
        });
    }
}
