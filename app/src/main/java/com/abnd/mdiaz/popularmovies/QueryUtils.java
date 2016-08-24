package com.abnd.mdiaz.popularmovies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by neboo on 23-Aug-16.
 */
public final class QueryUtils {

    //Hiding the API Key...
    private static final String POP_MOVIES_URL =
            String.format("http://api.themoviedb.org/3/movie/popular?api_key=%s&page=2",
                    SensitiveInfo.getApiKey());

    private static final String TOP_MOVIES_URL =
            String.format("http://api.themoviedb.org/3/movie/top_rated?api_key=%s&page=2",
                    SensitiveInfo.getApiKey());

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String SMALL_IMAGE_SIZE = "w92";
    private static final String MEDIUM_IMAGE_SIZE = "w185";
    private static final String LARGE_IMAGE_SIZE = "w500";

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static String getPopularMoviesUrl() {
        return POP_MOVIES_URL;
    }

    public static String getTopMoviesUrl() {
        return TOP_MOVIES_URL;
    }

    public static List<Movie> fetchMovieData(URL url) {

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractMovies(jsonResponse);
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Movie> extractMovies(String movieJson) {

        List<Movie> movieList = new ArrayList<>();

        try {
            //Whole thing
            JSONObject main = new JSONObject(movieJson);

            //The results array
            JSONArray results = main.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                //One movie
                JSONObject movieObject = results.getJSONObject(i);

                String name = movieObject.getString("title");
                String posterThumbnail = movieObject.getString("poster_path");
                String backdropImage = movieObject.getString("backdrop_path");
                String synopsis = movieObject.getString("overview");
                float rating = (float) movieObject.getDouble("vote_average");
                String releaseDate = movieObject.getString("release_date");

                //Getting a more readable date
                String formattedDate = "Release Date: " + dateFormat(releaseDate);

                String colorGenerationPath = IMAGE_BASE_URL + SMALL_IMAGE_SIZE + posterThumbnail;
                String fullPosterImagePath = IMAGE_BASE_URL + MEDIUM_IMAGE_SIZE + posterThumbnail;
                String fullBackdropImagePath = IMAGE_BASE_URL + LARGE_IMAGE_SIZE + backdropImage;

                Bitmap basicBitmap = getBitmapFromURL(colorGenerationPath);
                Palette palette = new Palette.Builder(basicBitmap).generate();

                int darkColor = palette.getDarkMutedColor(Color.parseColor("#0D47A1"));
                int lightColor = palette.getLightMutedColor(Color.parseColor("#2196F3"));

                movieList.add(new Movie(name, fullPosterImagePath, fullBackdropImagePath, synopsis,
                        rating, formattedDate, darkColor, lightColor));

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Movie JSON results", e);
        }

        return movieList;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    private static String dateFormat(String releaseDate) {

        String inputString = "yyyy-MM-dd";
        String outputString = "MMMM dd, yyyy";
        SimpleDateFormat parser = new SimpleDateFormat(inputString);
        SimpleDateFormat properForm = new SimpleDateFormat(outputString);

        String formattedDate = null;

        try {
            Date properDate = parser.parse(releaseDate);
            formattedDate = properForm.format(properDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
}
