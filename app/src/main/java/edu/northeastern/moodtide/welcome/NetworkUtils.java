package edu.northeastern.moodtide.welcome;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("X-RapidAPI-Key", "a8a75b3734mshbdb1bb5ffb9b062p1c98f7jsn40ad084fd0cd");
        urlConnection.setRequestProperty("X-RapidAPI-Host", "positivity-tips.p.rapidapi.com");
        urlConnection.setDoInput(true);
        urlConnection.connect();


        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            // convert this stream into a String, which represents the full response content.
            Scanner scanner = new Scanner(in);
            // sets the delimiter to the beginning of the input, effectively telling the Scanner to tokenize the entire stream as a single token.
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
