package com.example.mihail.showtime;

import android.app.Activity;
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
        //data_json = data_json.replaceAll("'","\"");

        return data_json;
    }
    private final String LOG_TAG = FetchDataTask.class.getSimpleName();

    List<Movie> da = null;
    DataAdapter adapter = null;
    Activity activity;
    Context ctx;
    String query;
    public FetchDataTask(Context ctx, List<Movie> da, DataAdapter adapter, String query)
    {

        this.ctx = ctx;
        this.da = da;
        this.adapter = adapter;
        activity = (Activity) ctx;
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

        if(query != null)
        {
            JSONObject allData = new JSONObject(dataStr);
            JSONObject data = allData.getJSONObject(OWM_LIST);
            JSONObject result = data.getJSONObject("results");
            JSONArray titles = result.getJSONArray("titles");
            String[] resultStrs = new String[20];
            for(int i = 0; i < titles.length(); i++) {
                JSONObject movie = titles.getJSONObject(i);
                resultStrs[i] = movie.getString("title")+ "::" + "0000"+"::"+ movie.getString("thumbnail")+"::" + movie.getString("id");
            }
            return resultStrs;
        }
        else
        {
            JSONObject allMovies = new JSONObject(dataStr);
            JSONObject movies = allMovies.getJSONObject(OWM_LIST);
            JSONArray movieArray = movies.getJSONArray(OWM_MOVIES);

            String[] resultStrs = new String[50];
            for(int i = 0; i < movieArray.length(); i++) {
                JSONObject movie = movieArray.getJSONObject(i);
                resultStrs[i] = movie.getString("title")+ "::" + movie.getString("year")+"::"+ movie.getString("urlPoster")+"::" + movie.getString("idIMDB")+"::" + movie.getString("rating")+"::" + movie.getInt("ranking");
            }
            return resultStrs;
        }
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

                urlString="http://imdb.wemakesites.net/api/search?q="+query+"&api_key=e6897044-a37a-4a2a-86b9-a1ee615b79fa";
            }
            else
            {
                urlString="https://protected-forest-20580.herokuapp.com/top50";
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
            dataStr = dataStr.replaceAll("\"\"","\"none\"");
            dataStr = JsonSanitizer.jsonSanitizeOne(dataStr);
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
    protected void onPostExecute(String[] result){
        if(result !=null){
            //mForecastAdapter.clear();
            adapter.clear();
            for(String d:result){
               if(d==null)
               {
                   break;
               }
                String[] strings = d.split("::");
                String imageBetterQuality = strings[2];
                int indexBegin = imageBetterQuality.indexOf("_CR") - 2;
                int indexEnd = imageBetterQuality.indexOf("_AL_");

                if(indexEnd !=-1)
                {
                    if(imageBetterQuality.charAt(indexBegin-1)=='Y')
                    {

                        imageBetterQuality = new StringBuilder(imageBetterQuality).replace(indexBegin,indexEnd,"200_CR0,0,130,200").toString();
                    }
                    else
                    {
                        imageBetterQuality = new StringBuilder(imageBetterQuality).replace(indexBegin,indexEnd,"250_CR0,0,200,200").toString();
                    }
                    Movie m = new Movie(strings[0],imageBetterQuality,Integer.parseInt(strings[1]),strings[3]);
                    da.add(m);
                }
                else if (query == null)
                {
                    Movie m = new Movie(strings[0],imageBetterQuality,Integer.parseInt(strings[1]),strings[3]);
                    m.setRanking(Integer.parseInt(strings[5]));
                    m.setRating(strings[4]);
                    da.add(m);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

}
