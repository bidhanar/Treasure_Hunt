package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    Button login;
//    Button signup;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        ColorDrawable cd = new ColorDrawable(Color.parseColor("#043348"));
        getSupportActionBar().setBackgroundDrawable(cd);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.theme));
        }

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );

        login = findViewById(R.id.login);
//        signup = findViewById(R.id.signup);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        if (ParseUser.getCurrentUser() != null){

            if(ParseUser.getCurrentUser().getUsername().toString().equals("ahmnc")){
                Intent intent = new Intent(MainActivity.this,admin.class);
                intent.putExtra("user",ParseUser.getCurrentUser().getUsername());
                startActivity(intent);

            }
            else{
                Intent intent = new Intent(MainActivity.this,currentLocation.class);
                intent.putExtra("user",ParseUser.getCurrentUser().getUsername());
                startActivity(intent);
            }
        }else{
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login(v);
                }
            });
//            signup.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    signUp(v);
//                }
//            });
        }

    }


//    public void signUp(View view){
//        if(username.getText().toString().matches("") || password.getText().toString().matches("")){
//            Toast.makeText(getApplicationContext(),"Username and Password are required!!", Toast.LENGTH_SHORT).show();
//        }else{
//            ParseUser user = new ParseUser();
//            user.setUsername(username.getText().toString());
//            user.setPassword(password.getText().toString());
//
//            user.signUpInBackground(new SignUpCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if (e == null){
//                        Log.i("Sign Up","Successful");
//                        Intent intent = new Intent(MainActivity.this,currentLocation.class);
//                        intent.putExtra("user",ParseUser.getCurrentUser().getUsername());
//                        startActivity(intent);
//                    }
//                    else{
//                        Log.i("SIgn Up","failed");
//                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }

    public void login(View view){
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null){
                    if(username.getText().toString().equals("ahmnc")){
                        Intent intent = new Intent(MainActivity.this,admin.class);
                        intent.putExtra("user",ParseUser.getCurrentUser().getUsername());
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, currentLocation.class);
                        intent.putExtra("user", ParseUser.getCurrentUser().getUsername());
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}