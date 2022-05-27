package com.example.pbl_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class ArtGallery extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_gallery);

        intent= new Intent(ArtGallery.this, ConfirmImage.class);

        ImageView taj1= findViewById(R.id.taj);
        ImageView taj2= findViewById(R.id.taj2);
        ImageView china= findViewById(R.id.wallOfChina);
        ImageView col= findViewById(R.id.colesium);
        ImageView matchu= findViewById(R.id.matchu);
        ImageView petra= findViewById(R.id.petra);
        ImageView pyrimid= findViewById(R.id.pyrymid);
        ImageView rio= findViewById(R.id.rio);


        taj1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("imageId", "" + R.drawable.taj);
                startActivity(intent);
                finish();
            }
        });
        taj2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("imageId", "" + R.drawable.taj_img);
                startActivity(intent);
                finish();
            }
        });
        china.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("imageId","" + R.drawable.wallofchina);
                startActivity(intent);
                finish();
            }
        });
        col.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("imageId","" + R.drawable.cloesium);
                startActivity(intent);
                finish();
            }
        });
        matchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("imageId","" + R.drawable.matchu);
                startActivity(intent);
                finish();
            }
        });
        petra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("imageId", "" + R.drawable.petra);
                startActivity(intent);
                finish();
            }
        });
        pyrimid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("imageId", "" + R.drawable.pyrymid);
                startActivity(intent);
                finish();
            }
        });
        rio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("imageId",  "" + R.drawable.rio);
                startActivity(intent);
                finish();
            }
        });



    }

}