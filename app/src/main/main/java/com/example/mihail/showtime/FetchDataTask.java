package com.example.mihail.showtime;

import android.content.Context;
import android.net.Uri;
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
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mihail on 8/14/16.
 */
public class FetchDataTask extends AsyncTask<Void,Void,String[]> {
    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        data_json = data_json.replace("{:\"","{title:\"");
        return data_json;
    }
    private final String LOG_TAG = FetchDataTask.class.getSimpleName();
    /*private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        return format.format(date).toString();
    }*/
    Context ctx = null;
    List<Movie> da = null;
    DataAdapter adapter = null;
    FetchDataTask(Context ctx, List<Movie> da, DataAdapter adapter)
    {
        this.ctx = ctx;
        this.da = da;
        this.adapter = adapter;
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */

    private String[] getWeatherDataFromJson(String dataStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "data";
        final String OWM_MOVIES = "movies";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DATETIME = "dt";
        final String OWM_DESCRIPTION = "main";
        dataStr = FetchDataTask.convertStandardJSONString(dataStr);
        JSONObject allMovies = new JSONObject(dataStr);
        JSONObject movies = allMovies.getJSONObject(OWM_LIST);
        JSONArray movieArray = movies.getJSONArray(OWM_MOVIES);

        String[] resultStrs = new String[50];
        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            resultStrs[i] = movie.getString("title")+ "::" + movie.getString("year")+"::"+ movie.getString("urlPoster");
            Log.v("RESULT",resultStrs[i]);
            //resultStrs[i] = day + " - " + description + " - " + highAndLow;
        }
        for(String s: resultStrs){
            Log.v(LOG_TAG, "Forecast entry: " + s);
        }

        return resultStrs;
    }
    @Override
    protected String[] doInBackground(Void... voids) {
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
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String urlString="https://protected-forest-20580.herokuapp.com/top50";


            URL url = new URL(urlString);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            dataStr = buffer.toString();
            Log.v(LOG_TAG, "Forecast JSON String: " +dataStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
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
    protected void onPostExecute(String[] result){
        if(result !=null){
            //mForecastAdapter.clear();
            adapter.clear();
            for(String d:result){
                String[] strings = d.split("::");
                Movie m = new Movie(strings[0],strings[2],Integer.parseInt(strings[1]));
                da.add(m);



            }
            adapter.notifyDataSetChanged();
            for(Movie d : da)
            {
                Log.v("CCC",d.toString());
            }
        }
    }

}
