package com.example.mihail.showtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StarterActivity extends AppCompatActivity {
    public Button btnSignIn, btnSignUp, btnSignInDialog;
    public LoginDataBaseAdapter loginDataBaseAdapter;
    public Dialog dialog;
    public EditText editTextUserName;
    public EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        btnSignIn = (Button) findViewById(R.id.buttonSignIN);
        btnSignUp = (Button) findViewById(R.id.buttonSignUP);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intentSignUP = new Intent(getApplicationContext(),
                        SignUPActivity.class);
                startActivity(intentSignUP);
            }
        });

        dialog = new Dialog(StarterActivity.this);
        dialog.setContentView(R.layout.login);
        dialog.setTitle("Login");

        btnSignInDialog = (Button) dialog.findViewById(R.id.buttonSignIn);


        editTextUserName = (EditText) dialog
                .findViewById(R.id.editTextUserNameToLogin);
        editTextPassword = (EditText) dialog
                .findViewById(R.id.editTextPasswordToLogin);

        LoginDataBaseAdapter lae = new LoginDataBaseAdapter(this);
        lae.open();
        lae.insertEntry("Nikola", "Nikola");


    }

    public void signIn(View V) {



        btnSignInDialog.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();
                String storedPassword = loginDataBaseAdapter
                        .getSinlgeEntry(userName);
                if (password.equals(storedPassword)) {
                    Toast.makeText(StarterActivity.this,
                            "Congrats: Login Successfull", Toast.LENGTH_LONG)
                            .show();
                    dialog.dismiss();
                    //((MyApplication) StarterActivity.this.getApplication()).setUserId(userName);

                    Intent main = new Intent(StarterActivity.this, MainActivity.class);
                    //Intent main = new Intent(StarterActivity.this, TrailerShowActivity.class);
                    main.putExtra("userId",userName);
                    startActivity(main);
                } else {
                    Toast.makeText(StarterActivity.this,
                            "User Name or Password does not match",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginDataBaseAdapter.close();
    }
}