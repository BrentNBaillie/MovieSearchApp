package com.example.moviesearchapp.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.moviesearchapp.R;
import com.example.moviesearchapp.model.Movie;
import com.example.moviesearchapp.api.API;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>
{
    public List<Movie> movies;
    private final OnItemClickListener listener;
    private Movie movie;
    private View view;
    private final API api;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    public Adapter(List<Movie> movies, OnItemClickListener listener, API api)
    {
        this.movies = movies;
        this.listener = listener;
        this.api = api;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type)
    {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        movie = movies.get(position);
        viewHolder.title.setText(movie.Title);
        viewHolder.year.setText(movie.Year);
        Glide.with(viewHolder.itemView.getContext()).load(movie.Poster).into(viewHolder.poster);
        api.getMovieDetails(movie.ImdbID);
    }

    @Override
    public int getItemCount()
    {
        if(movies == null)
        {
            return 0;
        }
        return movies.size();
    }

    public void updateList(List<Movie> movies)
    {
        this.movies = movies;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView year;
        TextView rating;
        ImageView poster;

        public ViewHolder(View view, OnItemClickListener listener)
        {
            super(view);
            title = view.findViewById(R.id.title_txt);
            year = view.findViewById(R.id.year_txt);
            rating = view.findViewById(R.id.rating_txt);
            poster = view.findViewById(R.id.poster_img);
            view.setOnClickListener(v -> listener.onItemClick(v, getAdapterPosition()));
        }
    }
}
