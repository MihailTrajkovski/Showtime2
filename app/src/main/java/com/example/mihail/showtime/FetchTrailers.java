package com.example.mihail.showtime;

/**
 * Created by mihail on 12.9.16.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mihail on 8/14/16.
 */
public class FetchTrailers extends AsyncTask<Void,Void,String> {
    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        data_json = data_json.replace("{:\"","{title:\"");
        //data_json = data_json.replaceAll("'","\"");

        return data_json;
    }
    private final String LOG_TAG = FetchDataTask.class.getSimpleName();

    List<Movie> da = null;
    DataAdapter adapter = null;
    ActorMovieActivity activity;
    Context ctx;
    String query;
    public FetchTrailers(ActorMovieActivity act, String query)
    {
        this.activity = act;
        this.query = query;
    }
    FetchTrailers(Context ctx,String query)
    {

        this.ctx = ctx;
        activity = (ActorMovieActivity) ctx;
        this.query = query;

    }


    private String getWeatherDataFromJson(String dataStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "data";
        final String OWM_MOVIES = "movies";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DATETIME = "dt";
        final String OWM_DESCRIPTION = "main";
        JSONObject allData = new JSONObject(dataStr);
        JSONObject data = allData.getJSONObject("data");
        JSONArray videos = data.getJSONArray("videos");
        JSONObject first = videos.getJSONObject(0);
        return first.getString("key");


    }
    @Override
    protected String doInBackground(Void... voids) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String dataStr = null;
        String format="json";
        String units="metric";
        int numDays=7;


        try {

            String urlString = "";
            urlString="http://www.myapifilms.com/tmdb/movieInfoImdb?idIMDB="+query+"&token=d71ebfc1-1d52-4571-ad89-7e19e14322f0&format=json&language=en&alternativeTitles=0&casts=0&images=0&keywords=0&releases=0&videos=1&translations=0&similar=0&reviews=0&lists=0";
            URL url = new URL(urlString);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            dataStr = buffer.toString();
            dataStr = FetchDataTask.convertStandardJSONString(dataStr);
            dataStr = dataStr.replaceAll("u'","'");
            dataStr = dataStr.replaceAll("u\"","\"");
            //dataStr = dataStr.replaceAll("\"\"","\"none\"");
            //dataStr = JsonSanitizer.jsonSanitizeOne(dataStr);
            Log.v(LOG_TAG, "Forecast JSON String: " +dataStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getWeatherDataFromJson(dataStr, numDays);
        }catch(JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result){
        activity.urlVideo = "https://www.youtube.com/watch?v="+result;
        activity.findViewById(R.id.videos).setEnabled(true);

    }

}
