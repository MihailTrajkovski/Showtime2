package com.example.mihail.showtime2;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.widget.EditText;

import com.example.mihail.showtime.AlarmManagerReceiver;
import com.example.mihail.showtime.BuildConfig;
import com.example.mihail.showtime.MainActivity;
import com.example.mihail.showtime.R;

import com.example.mihail.showtime.SampleIntentService;
import com.example.mihail.showtime.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.Robolectric;
import org.robolectric.internal.Shadow;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.*;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP )
@RunWith(RobolectricTestRunner.class)
public class ServiceTest {
    //проверува дали може да се инстанцира и тестира еден прост сервис
    @Test
    public void serviceTest() {
        Application application = RuntimeEnvironment.application;
        Intent expectedService = new Intent(application, SampleIntentService.class);

        AlarmManagerReceiver alarmManagerReceiver = new AlarmManagerReceiver();
        alarmManagerReceiver.onReceive(application, new Intent());

        Intent serviceIntent = Shadows.shadowOf(application).getNextStartedService();
        assertNotNull(serviceIntent);
        assertEquals(serviceIntent.getComponent(),
                expectedService.getComponent());
    }

}