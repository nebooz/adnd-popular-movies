package com.abnd.mdiaz.popularmovies;

/**
 * Created by neboo on 23-Aug-16.
 */
public class Movie {

    String mName;
    String mPosterThumbnail;
    String mSynopsis;
    float mRating;
    String mReleaseDate;

    public Movie(String name, String posterThumbnail, String synopsis, float rating, String releaseDate) {
        mName = name;
        mPosterThumbnail = posterThumbnail;
        mSynopsis = synopsis;
        mRating = rating;
        mReleaseDate = releaseDate;
    }

    public String getName() {
        return mName;
    }

    public String getPosterThumbnail() {
        return mPosterThumbnail;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public float getRating() {
        return mRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }
}
