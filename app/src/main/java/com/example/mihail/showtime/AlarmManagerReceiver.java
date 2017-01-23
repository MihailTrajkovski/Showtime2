package com.example.mihail.showtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by mihail on 21.1.17.
 */
public class AlarmManagerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, SampleIntentService.class);
        context.startService(intentService);

    }
};