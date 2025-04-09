package com.example.moviesearchapp.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Movie implements Serializable
{
    @SerializedName("Title") public String Title;
    @SerializedName("Year") public String Year;
    @SerializedName("imdbID") public String ImdbID;
    @SerializedName("Poster") public String Poster;
    @SerializedName("Production") public String Studio;
    @SerializedName("ImdbRating") public String ImdbRating;
    @SerializedName("Rated") public String Rating;
    @SerializedName("Plot") public String Plot;
}
