package com.abnd.mdiaz.popularmovies;

/**
 * Created by neboo on 23-Aug-16.
 */
public class Movie {

    private String mName;
    private String mPosterThumbnail;
    private String mBackdropImage;
    private String mSynopsis;
    private float mRating;
    private String mReleaseDate;

    public Movie(String name, String posterThumbnail, String backdropImage, String synopsis, float rating, String releaseDate) {
        mName = name;
        mBackdropImage = backdropImage;
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

    public String getBackdropImage() {
        return mBackdropImage;
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
