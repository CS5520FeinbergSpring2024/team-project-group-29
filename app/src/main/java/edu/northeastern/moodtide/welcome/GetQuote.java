package edu.northeastern.moodtide.welcome;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import edu.northeastern.moodtide.R;

public class GetQuote implements Runnable{
    private Activity activity;

    public GetQuote(Activity activity) {
        this.activity = activity;
    }
    @Override
    public void run() {
        try {
            // get quote
            String urlStr ="https://positivity-tips.p.rapidapi.com/api/positivity/affirmation";
            URL url = new URL(urlStr);
            String response = NetworkUtils.getResponseFromHttpUrl(url);
            JSONObject jsonResponse = new JSONObject(response);
            String quote  = jsonResponse.getString("affirmation");
            Log.e("QUOTE", quote);

            // update UI
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView quoteTextView = activity.findViewById(R.id.text_welcome);
                    quoteTextView.setText(quote);
                }
            });

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }








    }
}
