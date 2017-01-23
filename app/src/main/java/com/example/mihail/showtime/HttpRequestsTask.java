package com.example.mihail.showtime;

/**
 * Created by mihail on 12.9.16.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * Created by mihail on 12.9.16.
 */

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


/**
 * Created by mihail on 8/14/16.
 */
public class HttpRequestsTask extends AsyncTask<Void,Void,String[]> {
    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        data_json = data_json.replace("{:\"","{title:\"");
        //data_json = data_json.replaceAll("'","\"");

        return data_json;
    }

    public Boolean getHasBeenExecuted() {
        return hasBeenExecuted;
    }

    private Boolean hasBeenExecuted;
    List<Movie> da = null;
    DataAdapter adapter = null;
    Activity activity;
    Context ctx;
    String query;
    String urlToGet;

    public String getType() {
        return type;
    }

    String type;
    String[] results;
    public HttpRequestsTask(Context ctx, String url, String type)
    {
        hasBeenExecuted= false;

        this.ctx = ctx;
        activity = (Activity) ctx;
        this.query = query;
        this.urlToGet = url;
        this.type = type;
        results = new String[100];
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

            //urlString="https://protected-forest-20580.herokuapp.com/get/" + query;
            urlString=urlToGet;
            URL url = new URL(urlString);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(type);
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
            //dataStr = FetchDataTask.convertStandardJSONString(dataStr);
            //dataStr = dataStr.replaceAll("u'","'");
            //dataStr = dataStr.replaceAll("\"\"","\"none\"");
            //dataStr = JsonSanitizer.jsonSanitizeOne(dataStr);
            //Log.v(LOG_TAG, "Forecast JSON String: " +dataStr);
        } catch (IOException e) {
            //Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    //Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        /*try {
            return getWeatherDataFromJson(dataStr, numDays);
        }catch(JSONException e){
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }*/
        results  = new String[50];
        this.results[0] = dataStr;

        return null;
    }


    @Override
    protected void onPostExecute(String[] result){
        hasBeenExecuted = true;

        //Toast.makeText(ctx, results[0], Toast.LENGTH_LONG).show();

    }

}

