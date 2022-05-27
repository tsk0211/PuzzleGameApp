package com.example.pbl_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class ConfirmImage extends AppCompatActivity {

    ImageView imgV;
    TextView confirmBtn;
    Intent intent, toNext;
    Bitmap bitmap;
    byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image);

        imgV = (ImageView) findViewById(R.id.imageCheck);
        confirmBtn = (TextView) findViewById(R.id.confirmBtn);
        intent = (Intent) getIntent();
        toNext = new Intent(ConfirmImage.this, SelectDifficulty.class);


        if (intent.hasExtra("imageId")) {
            int id=  Integer.parseInt(intent.getStringExtra("imageId"));
            BitmapDrawable drawable= (BitmapDrawable) getDrawable(id);
            bitmap= drawable.getBitmap();
            imgV.setImageBitmap(bitmap);

            toNext.putExtra("imageId",""+ id);

        } else {
            if (intent.hasExtra("image")) {
                bytes = intent.getByteArrayExtra("image");
                Bitmap img = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imgV.setImageBitmap(img);

                toNext.putExtra("image", bytes);
            }
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(toNext);
            }
        });

    }


}