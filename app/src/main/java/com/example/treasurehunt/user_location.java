package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class user_location extends AppCompatActivity {
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    Button user_location;
    Boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);

        ColorDrawable cd = new ColorDrawable(Color.parseColor("#043348"));

        getSupportActionBar().setBackgroundDrawable(cd);
        getSupportActionBar().setTitle("Users/Locations");


        listView = findViewById(R.id.user_location_listview);
        user_location = findViewById(R.id.user_location);
        clicked = true;

        user_location.setText("Users/Locations");
        listView.setDivider(null);

        user_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlist(view);
            }
        });

    }

    public void userlist(View view){
        if(clicked){
            arrayList.clear();
            arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.text_items_layout, arrayList);
            listView.setAdapter(arrayAdapter);
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.addAscendingOrder("User");
            arrayList.clear();
            arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.text_items_layout, arrayList);
            listView.setAdapter(arrayAdapter);
            query.addAscendingOrder("username");
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null){
                        if(objects.size() > 0){
                            for(ParseUser user : objects){
                                if(user.getUsername().equals("ahmnc")){
                                    System.out.println("Admin");
                                }else {
                                    arrayList.add(user.getUsername());
                                }
                            }
                            if (arrayList.size() == 0){
                                arrayList.add("No Users Found!!!");
                            }
                            arrayList = new ArrayList<String>(new HashSet<>(arrayList));
                            Collections.sort(arrayList);
                            arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.text_items_layout, arrayList);
                            listView.setAdapter(arrayAdapter);
                            user_location.setText("Users");
                        }
                    }
                    else{
                        e.printStackTrace();
                    }
                }
            });
            clicked = false;
//            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                @Override
//                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    return false;
//                }
//            });
        }
        else{
            clicked = true;
            arrayList.clear();
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("locations");
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null){
                        if(objects.size() > 0){
                            for(ParseObject object : objects){
                                arrayList.add(object.getString("locations"));
                            }
                            arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.text_items_layout, arrayList);
                            listView.setAdapter(arrayAdapter);
                            user_location.setText("Locations");

                        }
                        else{
                            user_location.setText("No Locations Added!");
                        }
                    }
                    else{
                        Toast.makeText(user_location.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }

}