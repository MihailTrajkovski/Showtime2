package com.example.mihail.showtime;

/**
 * Created by mihail on 21.1.17.
 */
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class SampleIntentService extends IntentService {
    public SampleIntentService() {
        super("SampleIntentService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();

        editor.putString("Language", "ES").commit();
        editor.putInt("SearchItems",10).commit();
        editor.putString("SAMPLE_DATA", "sample data").commit();
        editor.apply();
    }
}