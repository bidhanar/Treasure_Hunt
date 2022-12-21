package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class user_management extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        ColorDrawable cd = new ColorDrawable(Color.parseColor("#043348"));
//        ColorDrawable cd = new ColorDrawable(R.drawable.header_background);
//        getSupportActionBar().setBackgroundDrawable(R.drawable.header_background);

        getSupportActionBar().setBackgroundDrawable(cd);
        getSupportActionBar().setTitle("User Management");

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.theme));
        }

    }
}