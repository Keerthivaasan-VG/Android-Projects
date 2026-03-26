package com.bustiming.animalanimation;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;
    MediaPlayer mediaPlayer;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.button);

        mediaPlayer = MediaPlayer.create(this, R.raw.aud);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Thread for Animation
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        imageView.post(new Runnable() {
                            @Override
                            public void run() {

                                animationDrawable = new AnimationDrawable();
                                animationDrawable.addFrame(getResources().getDrawable(R.drawable.img1), 3000);
                                animationDrawable.addFrame(getResources().getDrawable(R.drawable.img2), 3000);
                                animationDrawable.addFrame(getResources().getDrawable(R.drawable.img3), 3000);
                                animationDrawable.addFrame(getResources().getDrawable(R.drawable.img4), 3000);
                                animationDrawable.addFrame(getResources().getDrawable(R.drawable.img5), 3000);
                                animationDrawable.addFrame(getResources().getDrawable(R.drawable.img6), 3000);
                                animationDrawable.addFrame(getResources().getDrawable(R.drawable.img7), 3000);
                                animationDrawable.addFrame(getResources().getDrawable(R.drawable.img8), 3000);
                                animationDrawable.addFrame(getResources().getDrawable(R.drawable.img9), 3000);

                                animationDrawable.setOneShot(false);
                                imageView.setBackground(animationDrawable);
                                animationDrawable.start();
                            }
                        });

                    }
                }).start();


                // Thread for Music
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                mediaPlayer.start();
                            }
                        });
                    }
                }).start();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

