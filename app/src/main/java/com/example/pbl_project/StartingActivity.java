package com.example.pbl_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StartingActivity extends AppCompatActivity {

    ImageView iv;
    TextView tv;
    static Animation fadeIn, fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        iv = (ImageView) findViewById(R.id.imageView);
        tv= (TextView) findViewById(R.id.textView);

        fadeIn= AnimationUtils.loadAnimation(StartingActivity.this, android.R.anim.fade_in);
        fadeOut= AnimationUtils.loadAnimation(StartingActivity.this, android.R.anim.fade_out);

        fadeIn.setDuration(5000);
        iv.startAnimation(fadeIn);
        tv.startAnimation(fadeIn);

        fadeOut.setFillAfter(true);

        Runnable rb = () -> {
            sleep(3000);

            StartingActivity.fadeOut.setDuration(1500);
            iv.startAnimation(StartingActivity.fadeOut);
            tv.startAnimation(StartingActivity.fadeOut);

            sleep(1600);

            Intent toMain= new Intent(StartingActivity.this, Home.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(toMain);
        };

        new Thread(rb).start();

    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Toast.makeText(this, " Error Is " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}