package com.example.mihail.showtime2;

/**
 * Created by Nikola on 0021 21/01/17 .
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.Browser;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mihail.showtime.BuildConfig;
import com.example.mihail.showtime.LoginDataBaseAdapter;
import com.example.mihail.showtime.MainActivity;
import com.example.mihail.showtime.R;
import com.example.mihail.showtime.SignUPActivity;
import com.example.mihail.showtime.StarterActivity;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

//import android.support.test.InstrumentationRegistry;



@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)

public class StarterActivityTest {
    Button btnSignIn, btnSignUp, btnSignInDialog;
    LoginDataBaseAdapter loginDataBaseAdapter;
    StarterActivity activity;
    private Instrumentation.ActivityMonitor mBrowserActivityMonitor;
    private Dialog dialog;
    private EditText editTextUserName;
    private EditText editTextPassword;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(StarterActivity.class);
        dialog = activity.dialog;
        editTextUserName = activity.editTextUserName;
        editTextPassword = activity.editTextPassword;
        btnSignIn = activity.btnSignIn;
        btnSignInDialog = activity.btnSignInDialog;
        btnSignUp = activity.btnSignUp;
        loginDataBaseAdapter = activity.loginDataBaseAdapter;

    }

    //
    @Test
    public void testSignUpOnClick() {
        btnSignUp.performClick();

        Intent expectedIntent = new Intent(activity, SignUPActivity.class);

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);

        Intent actualIntent = shadowActivity.getNextStartedActivity();

        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    @Test
    public void checkForNulls() {
        assertNotNull(btnSignIn);
        assertNotNull(btnSignUp);
        assertNotNull(loginDataBaseAdapter);
    }

    // testirame dali vo bazata vekje postoechkiot user kje se povrze so negoviot password
    @Test
    public void checkIfExistingRecordReturnsResult() {
        ShadowApplication application = shadowOf(RuntimeEnvironment.application);


        LoginDataBaseAdapter lae = new LoginDataBaseAdapter(activity);
        lae.open();
        //lae.insertEntry("Nikola", "Nikola");
        String bla = lae.getSinlgeEntry("Nikola");
        assertEquals("Nikola", bla);

    }

    @Test
    public void checkIfWeCanAddNewRecord() throws SQLException {
        LoginDataBaseAdapter lae = new LoginDataBaseAdapter(activity);
        lae.open();
        SQLiteDatabase db = lae.getDatabaseInstance();

        String USERNAME = "Stefan";
        Cursor mCount= db.rawQuery("select count(*) from LOGIN where USERNAME='" + USERNAME + "'", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        assertEquals(count, 0);

    }

    @Test
    public void checkIfWeCanAddExistingRecord() {
        LoginDataBaseAdapter lae = new LoginDataBaseAdapter(activity);
        lae.open();
        SQLiteDatabase db = lae.getDatabaseInstance();

        String USERNAME = "Nikola";
        Cursor mCount= db.rawQuery("select count(*) from LOGIN where USERNAME='" + USERNAME + "'", null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);
        mCount.close();

        assertEquals(count, 1);
    }

    @Test
    public void checkClickingOnSignInBringsUpDialog() {
        //btnSignIn.performClick();

        activity.signIn(btnSignIn);

        assertTrue(dialog.isShowing());
    }

    @Test
    public void checkAttemptToLogInWIthCorrectCredentials() {
        //btnSignIn.performClick();

        activity.signIn(btnSignIn);

        editTextUserName.setText("Nikola");
        editTextPassword.setText("Nikola");

        btnSignInDialog.performClick();

        Intent expectedIntent = new Intent(activity, MainActivity.class);

        ShadowActivity shadowActivity = Shadows.shadowOf(activity);

        Intent actualIntent = shadowActivity.getNextStartedActivity();

        assertTrue(actualIntent.filterEquals(expectedIntent));
    }

    @Test
    public void checkAttemptToLogInWithIncorrectCredentials() {
        activity.signIn(btnSignIn);

        editTextUserName.setText("Stefan");
        editTextPassword.setText("Stefan");

        btnSignInDialog.performClick();

        assertTrue(dialog.isShowing());
    }




}
