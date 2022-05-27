package com.example.pbl_project;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Piece {
    int id, x, y, width, height, position;
    ImageView img;
    Bitmap bitmap;

    Piece() {
        this.id = -1;
    }

    Piece (int id, int width, int height, int position) {
        this.id= id;
        this.width= width;
        this.height= height;
        this.position= position;

    }

    Piece (int id, int position) {
        this.id= id;
        this.position= position;
    }

    Piece (int id, ImageView imageView, Bitmap bitmap, int width, int height, int position) {
        this.id = id;
        this.img = imageView;
        this.bitmap = bitmap;

        this.width = width;
        this.height = height;
        this.position = position;
    }








































    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap() {
        this.img.setImageBitmap(this.bitmap);
    }

    public void setImageView(ImageView imageView) {
        this.img = imageView;
    }

    public ImageView getImg() {
        return this.img;
    }

    public void setX(float x) {
        this.img.setX(x);
    }

    public void setY(float y)  {
        this.img.setY(y);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getId() {
        return this.id;
    }
}

