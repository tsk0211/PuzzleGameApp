package com.example.pbl_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SelectDifficulty extends AppCompatActivity {

    Intent previous, next;
    TextView easy, med, hard;
    byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_difficulty);

        previous= (Intent) getIntent();
        next= new Intent(SelectDifficulty.this, MainActivity.class);

        if (previous.hasExtra("image")) {
            bytes= previous.getByteArrayExtra("image");
            next.putExtra("image", bytes);
        }
        if (previous.hasExtra("imageId")) {
            next.putExtra("imageId", previous.getStringExtra("imageId"));
        }

        easy= (TextView) findViewById(R.id.easy);
        med= (TextView) findViewById(R.id.medium);
        hard= (TextView) findViewById(R.id.hard);

        // Setting OnClickListener
        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.putExtra("factor", "3");
                next.putExtra("time", "120");
                startActivity(next);
            }
        });

        med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.putExtra("factor", "5");
                next.putExtra("time", "300");
                startActivity(next);
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.putExtra("factor", "7");
                next.putExtra("time", "420");
                startActivity(next);
            }
        });


    }


}