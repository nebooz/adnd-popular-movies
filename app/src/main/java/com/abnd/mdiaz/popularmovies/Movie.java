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
    private int mDarkColor;
    private int mLightColor;

    public Movie(String name, String posterThumbnail, String backdropImage, String synopsis, float rating, String releaseDate, int darkColor, int lightColor) {
        mName = name;
        mBackdropImage = backdropImage;
        mPosterThumbnail = posterThumbnail;
        mSynopsis = synopsis;
        mRating = rating;
        mReleaseDate = releaseDate;
        mDarkColor = darkColor;
        mLightColor = lightColor;
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

    public int getDarkColor() {
        return mDarkColor;
    }

    public int getLightColor() {
        return mLightColor;
    }
}
