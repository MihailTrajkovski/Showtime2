package com.example.mihail.showtime2;

import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.Browser;
import android.test.ActivityUnitTestCase;
import android.util.Log;
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
import org.robolectric.shadows.ShadowToast;

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

/**
 * Created by Nikola on 0022 22/01/17 .
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class SIgnUPActivityTest {
    EditText editTextUserName,editTextPassword,editTextConfirmPassword;
    Button btnCreateAccount;
    SignUPActivity activity;

    LoginDataBaseAdapter loginDataBaseAdapter;
    String message = "";

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(SignUPActivity.class);

        // get Instance  of Database Adapter
        loginDataBaseAdapter=new LoginDataBaseAdapter(activity);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        // Get Refferences of Views
        editTextUserName=(EditText)activity.findViewById(R.id.editTextUserName);
        editTextPassword=(EditText)activity.findViewById(R.id.editTextPassword);
        editTextConfirmPassword=(EditText)activity.findViewById(R.id.editTextConfirmPassword);

        btnCreateAccount=(Button)activity.findViewById(R.id.buttonCreateAccount);

    }

    @Test
    public void checkEmptySignUpCredentialsReturnFIELD_VACANT() {
        editTextUserName.setText("");
        editTextPassword.setText("");
        editTextConfirmPassword.setText("");

        btnCreateAccount.performClick();

        // proverka dali Toast tekstot e FIELD_VACANT
        String text = ShadowToast.getTextOfLatestToast();
        assertEquals(text, "Field Vacant");
    }


    @Test
    public void checkNonMatchingPasswords() {
        editTextUserName.setText("Stefan");
        editTextPassword.setText("password1");
        editTextConfirmPassword.setText("password2");

        btnCreateAccount.performClick();

        // proverka dali Toast tekstot e Password does not match
        String text = ShadowToast.getTextOfLatestToast();
        assertEquals(text, "Password does not match");
    }

    @Test
    public void checkMatchingPasswords() {
        editTextUserName.setText("Stefan");
        editTextPassword.setText("password");
        editTextConfirmPassword.setText("password");

        btnCreateAccount.performClick();

        // proverka dali Toast tekstot e Account Successfully Created
        String text = ShadowToast.getTextOfLatestToast();
        assertEquals(text, "Account Successfully Created");
    }

    @Test
    public void checkSuccessfulSignUp() {
        String username = "Stefan";
        String password = "password";
        editTextUserName.setText(username);
        editTextPassword.setText(password);
        editTextConfirmPassword.setText(password);

        btnCreateAccount.performClick();

        // proverka dali Toast tekstot e Account Successfully Created
        String text = ShadowToast.getTextOfLatestToast();
        assertEquals(text, "Account Successfully Created");
        assertEquals(loginDataBaseAdapter.getSinlgeEntry(username), password);
    }
}
