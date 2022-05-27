package com.example.pbl_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class OnStart extends AppCompatActivity {

    ImageView cameraImg, galleryImg, artGalleryImg;
    TextView cameraTxt, galleryTxt, artGalleryTxt;
    View.OnClickListener cam, gall, art_gall;
    private static final int CAMERA_CODE= 100, GALLERY_CODE= 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_start);

        // The Initializing The Things On Screen
        cameraImg = (ImageView) findViewById(R.id.cameraImg);
        galleryImg = (ImageView) findViewById(R.id.galleryImg);
        artGalleryImg = (ImageView) findViewById(R.id.artGalleryImg);

        cameraTxt = (TextView) findViewById(R.id.cameraBtn);
        galleryTxt = (TextView) findViewById(R.id.galleryBtn);
        artGalleryTxt = (TextView) findViewById(R.id.artGalleryBtn);


        // The OnClickListener
        cam= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(camera, CAMERA_CODE);
                } catch (Exception e) {
                    Toast.makeText(OnStart.this, "Error Is In Camera " +  e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        gall= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery= new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                try {
                    startActivityForResult(Intent.createChooser(gallery, "Select Image"), GALLERY_CODE);
                } catch (Exception e) {
                    Toast.makeText(OnStart.this, "Error Is In Gallery Pick " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        art_gall= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(OnStart.this, ArtGallery.class);
                startActivity(intent);
            }
        };


        // The Setting Of OnClickListeners
        cameraImg.setOnClickListener(cam);
        galleryImg.setOnClickListener(gall);
        artGalleryImg.setOnClickListener(art_gall);

        cameraTxt.setOnClickListener(cam);
        galleryTxt.setOnClickListener(gall);
        artGalleryTxt.setOnClickListener(art_gall);

        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ByteArrayOutputStream _bs= new ByteArrayOutputStream();
        byte[] bytesImg;
        Intent intent= new Intent(OnStart.this, ConfirmImage.class);

        if (resultCode == RESULT_OK) {

            if (requestCode == CAMERA_CODE) {
                // Camera
                Bundle bd= data.getExtras();
                Bitmap image= (Bitmap) bd.get("data");
                if (image != null) {
                    image.compress(Bitmap.CompressFormat.PNG, 100, _bs);
                    bytesImg= _bs.toByteArray();

                    intent.putExtra("image", bytesImg);
                    startActivity(intent);
                }
            }
            else if (requestCode == GALLERY_CODE) {
                // Gallery
                Uri d= data.getData();
                Bitmap image= null;
                try {
                    image = (Bitmap) MediaStore.Images.Media.getBitmap(getContentResolver(), d);
                } catch (IOException e) {
                    Toast.makeText(this, "Error Is " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                if (image != null) {
                    image.compress(Bitmap.CompressFormat.PNG, 100, _bs);
                    bytesImg= _bs.toByteArray();

                    intent.putExtra("image", bytesImg);
                    startActivity(intent);
                }
            }

        } else {
            Toast.makeText(this, "The Selected Image Is Not Applicable For This Game...", Toast.LENGTH_LONG).show();
        }

    }




}