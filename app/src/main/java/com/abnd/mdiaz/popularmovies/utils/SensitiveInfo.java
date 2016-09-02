package com.abnd.mdiaz.popularmovies.utils;

public final class SensitiveInfo {
    private static final String MOVIES_API_KEY = "2a1298a63f8d85ef3c2aadce5d8052b0";
    private static final String YOUTUBE_API_KEY = "AIzaSyAfrpEU9vhBHC4RYE8C3XUXrgNoR-3v7iE";

    private SensitiveInfo() {

    }

    public static String getMoviesApiKey() {
        return MOVIES_API_KEY;
    }

    public static String getYoutubeApiKeyApiKey() {
        return YOUTUBE_API_KEY;
    }

}