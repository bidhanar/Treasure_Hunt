package com.example.treasurehunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class admin extends AppCompatActivity {

    EditText addteam;
    ListView listView;
    ArrayList<String> arrayList;
    ArrayList<String> locations;
    ArrayAdapter arrayAdapter;
    String currentLocation;
    String nextLocation;
    Button button;
    Boolean clicked;
    Boolean location_added;

    long  mLastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_admin);

        addteam = findViewById(R.id.addteam);
        listView = findViewById(R.id.listview_admin);
        button = findViewById(R.id.users);
        arrayList = new ArrayList<>();
        locations = new ArrayList<>();

        currentLocation = "home";
        nextLocation = "location1";
        clicked = true;
        location_added = false;

        ColorDrawable cd = new ColorDrawable(Color.parseColor("#043348"));
//        ColorDrawable cd = new ColorDrawable(R.drawable.header_background);
//        getSupportActionBar().setBackgroundDrawable(R.drawable.header_background);

        getSupportActionBar().setBackgroundDrawable(cd);
        getSupportActionBar().setTitle(title(currentLocation));

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.theme));
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("locations");
        query.addAscendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size() > 0){
                        location_added = true;
                        for(ParseObject object : objects){
                            locations.add(object.getString("locations"));
                            Toast.makeText(getApplicationContext(),object.getString("locations"), Toast.LENGTH_SHORT).show();
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

        showTeams();
        button.setText("Users");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin.this , user_management.class);
                startActivity(intent);

            }
        });


        listView.setDivider(null);
//        listView.setBackgroundResource(R.drawable.customshape);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
//                        Toast.LENGTH_SHORT).show();
                update(((TextView) view).getText().toString());
                return false;
            }
        });


    }

    public void logout(View view){
        ParseUser.logOut();
        Intent intent = new Intent(admin.this,MainActivity.class);
        startActivity(intent);
    }

    public void add(View view){

        hideKeybaord(view);

        if(location_added){
            ParseObject teams = new ParseObject("teams");

            if(addteam.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Empty Name!!",Toast.LENGTH_SHORT).show();
                showTeams();
                return;
            }
            teams.put("teams", addteam.getText().toString());

            for(String location : locations){
                teams.put(location , false);
            }

            teams.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {
                        Toast.makeText(getApplicationContext(), addteam.getText().toString() + " added successfully", Toast.LENGTH_SHORT).show();
                        addteam.setText("");
                        showTeams();
                    } else {
                        Toast.makeText(getApplicationContext(), addteam.getText().toString() + " adding failed due to " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else{
            Toast.makeText(getApplicationContext() , "No locations added!", Toast.LENGTH_SHORT).show();
        }





    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                showTeams();
                return true;
            case R.id.current:
                showLocation(currentLocation);
                return true;
            case R.id.location1:
                showLocation("location1");
                return true;
            case R.id.location2:
                showLocation("location2");
                return true;
            case R.id.location3:
                showLocation("location3");
                return true;
            case R.id.location4:
                showLocation("location4");
                return true;
            case R.id.location5:
                showLocation("location5");
                return true;
            case R.id.location6:
                showLocation("location6");
                return true;
            case R.id.location7:
                showLocation("location7");
                return true;
            case R.id.location8:
                showLocation("location8");
                return true;
            case R.id.location9:
                showLocation("location9");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public String title(String currentLocation){

        if(currentLocation == "home")
            return "Home";
        int curr = Integer.parseInt(String.valueOf(currentLocation.charAt(currentLocation.length() - 1)));
        return "Location " + String.valueOf(curr);

    }

    public void showTeams(){
        //Filling the listview
        currentLocation="home";
        getSupportActionBar().setTitle(title(currentLocation));
        arrayList.clear();
        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.text_items_layout, arrayList);
        listView.setAdapter(arrayAdapter);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("teams");
        query.addAscendingOrder("teams");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if(objects.size() > 0){
                        for(ParseObject object : objects){
                            arrayList.add(object.getString("teams"));
                        }
                        if (arrayList.size() == 0){
                            arrayList.add("No Teams Found!!!");
                        }
//                        HashSet<String> hashSet =
                        arrayList = new ArrayList<String>(new HashSet<>(arrayList));
                        Collections.sort(arrayList);
                        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.text_items_layout, arrayList);
                        listView.setAdapter(arrayAdapter);
//                        button.setText(arrayList.size()+" Team(s)");
                    }
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }

    public void showLocation(String id){
        arrayList.clear();
        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.text_items_layout, arrayList);
        listView.setAdapter(arrayAdapter);
        currentLocation = id;
        if(currentLocation.equals("home")){
            showTeams();
            return;
        }
        getSupportActionBar().setTitle(title(currentLocation));
        ParseQuery<ParseObject> query = ParseQuery.getQuery(id);
        query.addAscendingOrder(id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){
                    if(objects.size() > 0){
                        for(ParseObject object : objects){
                            arrayList.add(object.getString("location"));
//                            Toast.makeText(getApplicationContext(),"Location Tapped", Toast.LENGTH_SHORT).show();
                        }
                        if (arrayList.size() == 0){
                            arrayList.add("No Teams Found!!!");
                        }
                    }
                    arrayList = new ArrayList<String>(new HashSet<>(arrayList));
                    Collections.sort(arrayList);
                    arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.text_items_layout, arrayList);
                    listView.setAdapter(arrayAdapter);
//                    button.setText(arrayList.size()+" Team(s)");
                }
                else{
                    e.printStackTrace();
                }
            }
        });
    }

    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

    public void update(String name){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Pass this team?");
        builder.setMessage("This action cannot be undone");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                if(currentLocation.equals("home"))
                    nextLocation = "location1";
                else if(currentLocation.equals("location9"))
                    nextLocation = "location9";
                else{
                    int curr = Integer.parseInt(String.valueOf(currentLocation.charAt(currentLocation.length() - 1)));
                    nextLocation = "location" + String.valueOf(curr + 1);
                }

//                Toast.makeText(getApplicationContext(),String.valueOf(teamExist(name, nextLocation)), Toast.LENGTH_SHORT).show();
//
//
//                if(teamExist(name, nextLocation)){
//                    Toast.makeText(getApplicationContext(),"Team already exists",Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                    return;
//                }

                ParseObject location = new ParseObject(nextLocation);

                location.put("location", name);
                location.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            Toast.makeText(getApplicationContext(), " Added successfully", Toast.LENGTH_SHORT).show();

                            arrayList.remove(name);
                            arrayList = new ArrayList<String>(new HashSet<>(arrayList));
                            Collections.sort(arrayList);
                            arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.text_items_layout, arrayList);
                            listView.setAdapter(arrayAdapter);
//                            button.setText(arrayList.size()+" Team(s)");

                            String delete = currentLocation.equals("home") ? "teams" : "location";
                            String classs = currentLocation.equals("home") ? "teams" : currentLocation;
                            ParseQuery<ParseObject> deleteObject = ParseQuery.getQuery(classs);
                            deleteObject.whereEqualTo(delete,name);
                            deleteObject.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
//                                    Toast.makeText(getApplicationContext(),"Size of Array is " +String.valueOf(objects.size()),Toast.LENGTH_SHORT).show();
                                    objects.get(0).deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if(e == null){
                                                Toast.makeText(getApplicationContext(),"Done!",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "Adding failed due to " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
//
//                AlertDialog.Builder deleter = new AlertDialog.Builder(getApplicationContext());
//                deleter.setTitle("Delete?");
//                deleter.setMessage("This action cannot be undone");
//
//                deleter.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        arrayList.remove(name);
//                        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.text_items_layout, arrayList);
//                        listView.setAdapter(arrayAdapter);
//
//                        String delete = currentLocation.equals("home") ? "teams" : "location";
//                        String classs = currentLocation.equals("home") ? "teams" : currentLocation;
//                        ParseQuery<ParseObject> deleteObject = ParseQuery.getQuery(classs);
//                        deleteObject.whereEqualTo(delete,name);
//                        deleteObject.findInBackground(new FindCallback<ParseObject>() {
//                            @Override
//                            public void done(List<ParseObject> objects, ParseException e) {
////                                    Toast.makeText(getApplicationContext(),"Size of Array is " +String.valueOf(objects.size()),Toast.LENGTH_SHORT).show();
//                                objects.get(0).deleteInBackground(new DeleteCallback() {
//                                    @Override
//                                    public void done(ParseException e) {
//                                        if(e == null){
//                                            Toast.makeText(getApplicationContext(),"Done!",Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//
//                            }
//                        });
//
//                        AlertDialog alert = deleter.create();
//                        alert.show();
//
//                    }
//                });
//
//                deleter.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialog.dismiss();
//                    }
//                });
//
//                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void userlist(View view){
        if(clicked){
        arrayList.clear();
        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.text_items_layout, arrayList);
        listView.setAdapter(arrayAdapter);
        getSupportActionBar().setTitle("Users");
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
                            }
                        }
                        else{
                            e.printStackTrace();
                        }
                    }
                });
            clicked = false;
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    return false;
                }
            });
        }
        else{
            clicked = true;
            showTeams();
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
//                        Toast.LENGTH_SHORT).show();
                    update(((TextView) view).getText().toString());
                    return false;
                }
            });

        }

    }

}