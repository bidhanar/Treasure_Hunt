package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;

public class user_management extends AppCompatActivity {


    Button add_user_location;
    Button view;
    TextView orbutton;
    EditText add_username;
    EditText add_password;
    ArrayList<String> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);


        add_user_location = findViewById(R.id.add_user_location);
        orbutton = findViewById(R.id.or_button);
        add_username = findViewById(R.id.add_username);
        add_password = findViewById(R.id.add_password);
        view = findViewById(R.id.view);
        locations = new ArrayList<>();

        ColorDrawable cd = new ColorDrawable(Color.parseColor("#043348"));
//        ColorDrawable cd = new ColorDrawable(R.drawable.header_background);
//        getSupportActionBar().setBackgroundDrawable(R.drawable.header_background);

        getSupportActionBar().setBackgroundDrawable(cd);
        getSupportActionBar().setTitle("User Management");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("locations");
        query.addAscendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){
                        for(ParseObject object : objects){
                            locations.add(object.getString("locations"));
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"No locations added", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.theme));
        }


        //add functionality to text or_button
        orbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change(view);
            }
        });

        //add user and location adding facility
        add_user_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orbutton.getText().toString().equals("Add Location")){
                    add_user(view);

                }
                else{
                    add_location(view);
                }
            }
        });

        //view the user page
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_management.this , user_location.class);
                startActivity(intent);
            }
        });

    }

    public void change(View view){
        if(orbutton.getText().toString().equals("Add Location")){
            orbutton.setText("Add User");
            add_password.setVisibility(View.INVISIBLE);
            add_username.setHint("Location");
            add_user_location.setText("Add Location");
        }
        else{
            orbutton.setText("Add Location");
            add_password.setVisibility(View.VISIBLE);
            add_username.setHint("Username");
            add_user_location.setText("Add User");
        }
    }

    public void add_user(View view){
        if(add_username.getText().toString().matches("") || add_password.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(),"Username and Password are required!!", Toast.LENGTH_SHORT).show();
        }else{
            ParseUser user = new ParseUser();
            user.setUsername(add_username.getText().toString());
            user.setPassword(add_password.getText().toString());
            for(String location : locations){
                user.put(location,false);
            }

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        Toast.makeText(getApplicationContext(),"User added successfully!",Toast.LENGTH_SHORT).show();
                        add_username.setText("");
                        add_username.setHint("Username");
                        add_password.setText("");
                        add_password.setHint("Password");
                    }
                    else{
                        Log.i("Sign Up","failed");
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void add_location(View view){
        if(add_username.getText().toString().matches("")){
            Toast.makeText(getApplicationContext(),"Add valid location",Toast.LENGTH_SHORT).show();
        }
        else{
//            ParseObject parseObject = new ParseObject("teams");
            ParseObject parseObject = new ParseObject("locations");
            parseObject.put("locations",add_username.getText().toString().replaceAll("\\s", ""));
            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
                        Toast.makeText(getApplicationContext(),"Location added successfully",Toast.LENGTH_SHORT).show();
                        add_username.setText("");
                        add_username.setHint("Location");
                    }
                    else{
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
//            location.put("locations" , add_username.getText().toString().replaceAll("\\s", ""));
//            location.saveInBackground();
        }
    }


}