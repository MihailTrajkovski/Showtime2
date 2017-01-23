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
import android.widget.ImageView;
import android.widget.ScrollView;
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
public class FetchMovie extends AsyncTask<Void,Void,String[]> {
    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        data_json = data_json.replace("{:\"","{title:\"");

        return data_json;
    }
    private final String LOG_TAG = FetchDataTask.class.getSimpleName();

    List<Movie> da = null;
    DataAdapter adapter = null;
    ActorMovieActivity activity;
    Context ctx;
    String query;
    public FetchMovie(Context ctx, String query)
    {

        this.ctx = ctx;
        activity = (ActorMovieActivity) ctx;
        this.query = query;
    }


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
        JSONObject allData = new JSONObject(dataStr);
        JSONObject data = allData.getJSONObject(OWM_LIST);
        //JSONObject image = data.getJSONObject("image");
        //JSONObject description = data.getJSONObject("description");
        //JSONObject duration = data.getJSONObject("duration");
        //JSONArray titles = result.getJSONArray("titles");
        //String[] resultStrs = new String[20];
        //for(int i = 0; i < titles.length(); i++) {
        //    JSONObject movie = titles.getJSONObject(i);
        //    resultStrs[i] = movie.getString("title")+ "::" + "0000"+"::"+ movie.getString("thumbnail")+"::" + movie.getString("id");
       // }
        String[] strings = new String[6];
        strings[0] = data.getString("duration");
        strings[1] = data.getString("description");
        strings[2] = data.getString("released");
        strings[3] = data.getString("image");
        JSONObject js = data.getJSONObject("review");
        strings[4] = "Rating" + js.getString("rating") + "\n"+ js.getString("text");
        JSONArray jsonArray = data.getJSONArray("cast");
        Log.v("EEEEEEEY",String.valueOf(jsonArray.length()));
        for(int i =0;i<jsonArray.length();i++)
        {
            Log.v("EEEEEEEY",String.valueOf(jsonArray.getString(i)));


            activity.list.add(jsonArray.getString(i));
        }
        return strings;


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
            String urlString = "";
            if(query!= null){
                query = query.replaceAll(" ","_");
                urlString="http://imdb.wemakesites.net/api/" + query+"?api_key=e6897044-a37a-4a2a-86b9-a1ee615b79fa";
            }
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
    protected void onPostExecute(String[] result) {
        if (result != null) {
            //mForecastAdapter.clear();
            TextView txt = (TextView) this.activity.findViewById(R.id.descriptionText);
            txt.setText(result[1]);
            TextView txt2 = (TextView) this.activity.findViewById(R.id.duration);
            txt2.setText("Duration: " + result[0]);
            TextView txt3 = (TextView) this.activity.findViewById(R.id.released);
            txt3.setText("Released: " + result[2]);
            ImageView imgView = (ImageView) this.activity.findViewById(R.id.imageViewTitle);
            TextView txt4 = (TextView) this.activity.findViewById(R.id.reviewText);
            txt4.setText(result[4]);
            String imageBetterQuality = result[3];

                Picasso
                        .with(ctx)
                        .load(imageBetterQuality)
                        .resize(1020, 830)

                        .placeholder(R.drawable.ic_open_search)
                        .into(imgView);

                activity.arrayAdapter.notifyDataSetChanged();
                ScrollView scrollView = (ScrollView) activity.findViewById(R.id.scrollView);
                scrollView.setVerticalScrollbarPosition(activity.position);





        }

    }

}
