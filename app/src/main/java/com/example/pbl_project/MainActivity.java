package com.example.pbl_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.media.AudioManager;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int FACTOR, SQUARE, TIME;
    Intent pvi;
    byte[] imageBytes;
    Intent toHome, toAd;
    Bitmap mainImage, tempBitmap;
    RelativeLayout layout;
    View.OnClickListener onClickListener;
    Piece[] pieces, shuffledArray;
    int[] ideal, songList;
    Piece selected1, selected2;
    int screenWidth, screenHeight, maxVolume, currentVolume;
    boolean firstSelect= false;
    AnimatorSet animatorSet;
    Dialog dialog, doubleDialog, songD, winD;
    ObjectAnimator oA1, oA2, oA3, oA4, oA5;
    MediaPlayer mediaPlayer;
    ImageView settingBtn, previewImg;
    SeekBar volumeSeekBar;
    AudioManager audioManager;
    Button preview;
    TextView timerLabel, closeBtn, pv_closeBtn;
    SwitchCompat muteOrNot;
    ProgressBar progressBar;
    Thread timerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout= findViewById(R.id.imageLayout);

        toHome= new Intent(MainActivity.this, Home.class);
        toAd= new Intent(MainActivity.this, AdPage.class);

        timerLabel= findViewById(R.id.timer_digital);
        this.progressBar= findViewById(R.id.progressBar);

        screenHeight= Resources.getSystem().getDisplayMetrics().heightPixels;
        screenWidth= Resources.getSystem().getDisplayMetrics().widthPixels;

        pvi= getIntent();
        dialog= new Dialog(MainActivity.this);
        doubleDialog = new Dialog(MainActivity.this);
        songD= new Dialog(MainActivity.this);
        winD= new Dialog(MainActivity.this);

        settingBtn= findViewById(R.id.settings);

        if (pvi.hasExtra("image")) {
            imageBytes= pvi.getByteArrayExtra("image");
            mainImage= (Bitmap) BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        if (pvi.hasExtra("imageId")) {
            int id= Integer.parseInt(pvi.getStringExtra("imageId"));
            BitmapDrawable bitmapDrawable= (BitmapDrawable) getDrawable(id);
            Bitmap k = bitmapDrawable.getBitmap();

            mainImage= (Bitmap) k;

        }
        if (pvi.hasExtra("factor")) {
            String num= pvi.getStringExtra("factor");
            FACTOR= Integer.parseInt(num);
            SQUARE= FACTOR*FACTOR;
        }
        if (pvi.hasExtra("time")) {
            String t= pvi.getStringExtra("time");
            TIME= Integer.parseInt(t);
        }

        // Setting Ideal Case
        pieces= new Piece[SQUARE];
        ideal = new int[SQUARE];
        shuffledArray= new Piece[SQUARE];

        for (int l= 0; l< FACTOR*FACTOR; l++)
            ideal[l] = l+1;

        this.onClickListener= new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swappingAnimation(view);
            }
        };

        try {
            createPieces(mainImage);
            shuffledArray= shuffleArray(pieces.clone());
            setAllPiecesOnClickListener();
            setXY();
        } catch (Exception e) {
            Toast.makeText(this, "Error Is @1234 " + e.getMessage(), Toast.LENGTH_LONG).show();
        }



        // Setting Music
        songList= new int[] {R.raw.aspire, R.raw.now, R.raw.never};

        mediaPlayer = MediaPlayer.create(MainActivity.this, songList[0]);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer.start();
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    settingPress();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error Is @123" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Music Variables
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        // Timer
        timer(TIME);

    }

    private void setAllPiecesOnClickListener() {
        for (int index= 0; index< SQUARE; index++) {
            pieces[index].getImg().setOnClickListener(this.onClickListener);
        }
    }

    public void createPieces(Bitmap mainImage) {
        int imgW= mainImage.getWidth();
        int imgH= mainImage.getHeight();

        if (imgW > 500) {
            imgW = 500;
            mainImage= Bitmap.createScaledBitmap(mainImage, 500, mainImage.getHeight(), false);
        }
        if (imgH > 700) {
            imgH = 700;
            mainImage= Bitmap.createScaledBitmap(mainImage, mainImage.getWidth(), 700, false);
        }

        int wi= imgW / FACTOR;
        int hi= imgH / FACTOR;

        int w = (int) ((0.9 * screenWidth) / FACTOR);
        int h = (int) ((0.7 * screenHeight) / FACTOR);


        for (int i= 0; i< SQUARE; i++) {
            tempBitmap= Bitmap.createBitmap(mainImage, (i%FACTOR) * wi, (i/FACTOR) * hi, wi, hi);

            tempBitmap= Bitmap.createScaledBitmap(tempBitmap, w, h, false);

            pieces[i] = new Piece(i+1, null, tempBitmap, (i%FACTOR) * w, (i/FACTOR) * h, i);
            pieces[i].img= new ImageView(MainActivity.this);
            pieces[i].img.setMinimumWidth(w);
            pieces[i].img.setMinimumHeight(h);
            pieces[i].img.setImageBitmap(tempBitmap);

            pieces[i].getImg().setAlpha(0.75f);
            pieces[i].getImg().setId(i+1024);

            layout.addView(pieces[i].img);
        }
    }

    public void swappingAnimation(View view) {
        try {
            if (firstSelect) {
                firstSelect= false;
                selected2= getPieceByImageView(findViewById(view.getId()));

                if (isOkToSwap(selected2.position, selected1.position, this.FACTOR)) {
                    swap();
                    alphaToNormal();

                    if (checkWin(ideal, shuffledArray)) {
                        gameWin();
                    }


                } else {
                    // Not Okay To Swap
                    errorAnimation(selected1.getImg());
                    errorAnimation(selected2.getImg());
                }

            } else {
                firstSelect= true;
                selected1= getPieceByImageView(findViewById(view.getId()));
                selected1.getImg().setAlpha(0.85f);
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error Is 845 " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public Piece getPieceByImageView(ImageView imageView) {
        int index=0;

        for (int k= 0; k<  pieces.length; k++) {
            if (pieces[k].img.equals(imageView))
                break;
            index++;
        }
        if (index == 25) {
            index= 0;
        }
        return pieces[index];
    }

    public boolean isOkToSwap(int first, int second, int factor) {
        if (((first-1)/factor) == ((second-1)/factor)) {
            if ((first-1 == second) || (second-1 == first) || (first+1 == second) || (second+1 == first))
                return true;
        } else if ((first-factor == second) || (second-factor == first)) {
            return true;
        } else if ((first+factor == second) || (second+factor == first)) {
            return true;
        }
        return false;
    }

    public void errorAnimation(ImageView imageView) {
        float x = imageView.getX();

        oA1 = ObjectAnimator.ofFloat(imageView, "x", x, x-10);
        x = x - 10;
        oA2 = ObjectAnimator.ofFloat(imageView, "x", x, x+20);
        x = x + 20;
        oA3 = ObjectAnimator.ofFloat(imageView, "x", x, x-15);
        x = x - 15;
        oA4 = ObjectAnimator.ofFloat(imageView, "x", x, x+10);
        x = x + 10;
        oA5 = ObjectAnimator.ofFloat(imageView, "x", x, x-5);

        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(oA1, oA2, oA3, oA4, oA5);
        animatorSet.start();

        oA1= null;
        oA2= null;
        oA3= null;
        oA4= null;
        oA5= null;
    }

    public void gameWin() {
        sleep(3000);

        winD.setContentView(R.layout.win_pop);
        winD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        winD.setCancelable(false);

        Button btn= winD.findViewById(R.id.btn_to_home);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryAgainFunc(view);
            }
        });

        winD.show();

        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    public boolean checkWin(int[] arr1, Piece[] arr2) {
        for (int i= 0; i< arr1.length; i++)
            if (arr1[i] != arr2[i].id)
                return false;
        return true;
    }

    public Piece[] shuffleArray(Piece[] arr) {
        Random random = new Random();
        int ri, li;
        for (int k= 0; k< arr.length; k++) {
            ri = random.nextInt(FACTOR*FACTOR-1);
            li = random.nextInt(FACTOR*FACTOR-1);

            swap(arr, ri, li);
        }
        return arr;
    }

    public void swap(Piece[] arr, int from, int to) {
        Piece temp = arr[from];
        arr[from] = arr[to];
        arr[to] = temp;
        temp= null;
    }

    public void swap() {
        float x1, x2, y1, y2;
        int tempPos, t1, t2;
        Piece tempPiece;

        x1 = selected1.getImg().getX();
        x2 = selected2.getImg().getX();

        y1 = selected1.getImg().getY();
        y2 = selected2.getImg().getY();

        tempPos = selected1.getPosition();
        selected1.setPosition(selected2.getPosition());
        selected2.setPosition(tempPos);

        t1 = getIndex(shuffledArray, selected1);
        t2 = getIndex(shuffledArray, selected2);

        tempPiece = shuffledArray[t1];
        shuffledArray[t1] = shuffledArray[t2];
        shuffledArray[t2] = tempPiece;

        selected1.getImg().animate().x(x2).y(y2).setDuration(1000);
        selected2.getImg().animate().x(x1).y(y1).setDuration(1000);
    }

    public int getIndex(Piece[] arr, Piece piece) {
        int index= 0;
        for (index= 0; index< arr.length; index++) {
            if (piece.equals(arr[index])) {
                return index;
            }
        }
        return -1;
    }

    public void setXY() {
        float x, y;
        int w = (int) ((0.9 * screenWidth) / FACTOR);
        int h = (int) ((0.7 * screenHeight) / FACTOR);

        for (int i= 0; i< SQUARE; i++) {
            x = (float) (0.05 * screenWidth + (i % FACTOR) * w);
            y = (float) ((i / FACTOR) * h);
            shuffledArray[i].img.setX(x);
            shuffledArray[i].img.setY(y);
            shuffledArray[i].setPosition(i+1);
        }

        this.alphaToNormal();
    }

    public void alphaToNormal() {
        for (Piece piece : shuffledArray) {
            if (piece.getId() == piece.getPosition()) {
                piece.getImg().setAlpha(1.0f);
            } else {
                piece.getImg().setAlpha(0.75f);
            }
        }
        if (selected1 != null) {
            if (selected1.getId() == selected1.getPosition()) {
                selected1.getImg().setAlpha(1.0f);
            } else {
                selected1.getImg().setAlpha(0.75f);
            }
        }
        if (selected2 != null) {
            if (selected2.getId() == selected2.getPosition()) {
                selected2.getImg().setAlpha(1.0f);
            } else {
                selected2.getImg().setAlpha(0.75f);
            }
        }

    }

    public void settingPress() {
        dialog.setContentView(R.layout.settings);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar = dialog.findViewById(R.id.audioLevel);
        closeBtn= dialog.findViewById(R.id.closeBtn);
        muteOrNot= dialog.findViewById(R.id.mute_or_not);
        preview= dialog.findViewById(R.id.preview);
        Button btn= dialog.findViewById(R.id.browseBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (MainActivity.this.dialog.isShowing())
                        MainActivity.this.dialog.dismiss();

                    Dialog dia = MainActivity.this.songD;
                    dia.setContentView(R.layout.song_change);
                    dia.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dia.setCancelable(false);

                    TextView song1 = dia.findViewById(R.id.song_1);
                    TextView song2 = dia.findViewById(R.id.song_2);
                    TextView song3 = dia.findViewById(R.id.song_3);
                    TextView tv = dia.findViewById(R.id.close_window);

                    song1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainActivity.this.playSong(0);
                        }
                    });
                    song2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainActivity.this.playSong(1);
                        }
                    });
                    song3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainActivity.this.playSong(2);
                        }
                    });
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (dia.isShowing()) {
                                dia.dismiss();
                            }
                        }
                    });

                    dia.show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Eroor I :-> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);

        muteOrNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.this.mediaPlayer.isPlaying()) {
                    MainActivity.this.mediaPlayer.stop();
                } else {
                    MainActivity.this.mediaPlayer.start();
                }
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing())
                    dialog.cancel();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (dialog.isShowing())
                        dialog.cancel();
                    if (dialog.isShowing())
                        dialog.dismiss();

                    doubleDialog.setContentView(R.layout.preview);
                    doubleDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    doubleDialog.setCancelable(false);

                    pv_closeBtn = doubleDialog.findViewById(R.id.close_in_prev);
                    previewImg = doubleDialog.findViewById(R.id.previewImg);

                    pv_closeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (doubleDialog.isShowing())
                                doubleDialog.cancel();
                        }
                    });
                    previewImg.setImageBitmap(mainImage);

                    doubleDialog.show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error Is 11 " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }

    public void timer(int time) {
        Runnable timerActivity = new Runnable() {
            @Override
            public void run() {
                int second= time;
                int min, sec;
                String timeInWords= "";

                while (second > 0) {
                    second = second - 1;
                    sleep(999);
                    min= (int) (second / 60);
                    sec= (int) (second % 60);
                    timeInWords= " " + min + " : " + sec;

                    int finalSecond = second;
                    String finalTimeInWords = timeInWords;

                    MainActivity.this.runOnUiThread(() -> {changingProgress(finalSecond, time, finalTimeInWords);});


                }
                Runnable display = new Runnable() {
                    @Override
                    public void run() {
                        timesUpAnimation();
                    }
                };
                MainActivity.this.runOnUiThread(display);
            }
        };

        timerThread= new Thread(timerActivity);
        timerThread.start();
    }

    public void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Log.d("time error", "something happened");
        }
    }

    public void timesUpAnimation() {
        if (dialog.isShowing())
            dialog.dismiss();
        if (doubleDialog.isShowing())
            doubleDialog.dismiss();

        dialog.setContentView(R.layout.times_up_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    public void changingProgress(int sec, int totalTime, String timeLabel) {
        double ratio= (double) sec/totalTime;
        int progress= (int) (ratio * 100);

        if (sec == 30) {
            this.progressBar.setProgressDrawable(AppCompatResources.getDrawable(MainActivity.this, R.drawable.progress_bar_stress));
            this.timerLabel.setTextColor(Color.YELLOW);
        }
        if (sec == 10) {
            this.progressBar.setProgressDrawable(AppCompatResources.getDrawable(MainActivity.this, R.drawable.progress_bar_panic));
            this.timerLabel.setTextColor(Color.RED);
        }

        this.progressBar.setProgress(progress);
        this.timerLabel.setText(timeLabel);
    }

    public void tryAgainFunc(View view) {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        startActivity(toHome);
    }

    public void watchVideoFunc(View view) {
        if (mediaPlayer.isPlaying())
            mediaPlayer.stop();
        startActivity(toAd);
    }

    public void playSong(int id) {
        try {
            if (this.mediaPlayer.isPlaying())
                this.mediaPlayer.release();

            this.mediaPlayer = MediaPlayer.create(MainActivity.this, this.songList[id]);
            this.mediaPlayer.start();
        } catch (Exception e) {
            Toast.makeText(this, "Error Is -> " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}