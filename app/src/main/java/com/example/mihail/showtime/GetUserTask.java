package com.example.mihail.showtime;

/**
 * Created by mihail on 12.9.16.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
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
public class GetUserTask extends AsyncTask<Void,Void,String[]> {
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
    public boolean value = false;
    List<Movie> da = null;
    DataAdapter adapter = null;
    ActorMovieActivity activity;
    Context ctx;
    String query;
    String param;
    public GetUserTask(ActorMovieActivity activity, String query, String param)
    {
        this.param = param;
        this.activity =  activity;
        this.query = query;
    }
    GetUserTask(Context ctx,String query,String param)
    {
        this.param = param;
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
        ArrayList<String> favorites =new ArrayList<>();
        ArrayList<String> reviews =new ArrayList<>();
        JSONArray favoriteJson = allData.getJSONArray("favorite");
        for(int i =0;i<favoriteJson.length();i++)
        {
            JSONObject favorite = favoriteJson.getJSONObject(i);
            favorites.add(favorite.getString("movie"));
        }
        JSONArray reviewJson = allData.getJSONArray("review");
        for(int i =0;i<reviewJson.length();i++)
        {
            JSONObject review = reviewJson.getJSONObject(i);
            reviews.add(review.getString("username")+":\n" + review.getString("review"));

        }
        ArrayList<String> ratings =new ArrayList<>();
        JSONArray ratingJson = allData.getJSONArray("rating");
        for(int i =0;i<ratingJson.length();i++)
        {
            JSONObject rating= ratingJson.getJSONObject(i);
            ratings.add(rating.getString("movie"));
            ratings.add(String.valueOf(rating.getInt("rating")));
        }
        ArrayList<String> toWatch =new ArrayList<>();
        JSONArray toWatchJson = allData.getJSONArray("toWatch");
        for(int i =0;i<toWatchJson.length();i++)
        {
            JSONObject toWatchJsonJSONObject = toWatchJson.getJSONObject(i);
            toWatch.add(toWatchJsonJSONObject.getString("movie"));
        }

        StringBuilder sb1 = new StringBuilder();
        //String strings1 = "";
        for(String str : favorites)
        {
            sb1.append(" ");
            sb1.append(str);
        }
        StringBuilder sb2 = new StringBuilder();
        //String strings1 = "";
        int i =0;
        for(String str : ratings)
        {
            sb2.append(" ");
            sb2.append(str);
            i++;
        }
        StringBuilder sb3 = new StringBuilder();
        //String strings1 = "";
        for(String str : toWatch)
        {
            sb3.append(" ");
            sb3.append(str);
        }
        StringBuilder sb4 = new StringBuilder();
        for(String str : reviews)
        {
            sb4.append(str);
            sb4.append("\n\n");
        }
        String[] strings = new String[4];
        strings[0] = sb1.toString();
        strings[1] = sb2.toString();
        strings[2] = sb3.toString();
        strings[3] = sb4.toString();
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
                //query = query.replaceAll(" ","_");
                urlString="https://protected-forest-20580.herokuapp.com/user/" + query + "?movieId="+param;
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
            TextView txtView = (TextView)activity.findViewById(R.id.showReviews);
            txtView.setText(result[3]);
            TextView txt = (TextView)this.activity.findViewById(R.id.hiddenFavorites);
            Log.v("TXT1",result[0]);
            txt.setText(result[0]);
            Log.v("SETTXT1",txt.getText().toString());

            TextView txt1 = (TextView)this.activity.findViewById(R.id.hiddenRated);
            txt1.setText(result[1]);
            Log.v("TXT2",result[1]);
            Log.v("SETTXT2",txt1.getText().toString());
            TextView txt2 = (TextView)this.activity.findViewById(R.id.hiddenToWatch);
            txt2.setText(result[2]);
            String str = activity.getClass().getSimpleName();
            if(str.equals("ActorMovieActivity"))
            {
                ActorMovieActivity tmp = (ActorMovieActivity)activity;
                tmp.setUpAfterAsync();

            }
        }
    }

}
