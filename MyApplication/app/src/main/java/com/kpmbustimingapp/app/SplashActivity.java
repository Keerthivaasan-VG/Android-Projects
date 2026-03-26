package com.kpmbustimingapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.kpmbustimingapp.app.MainActivity;
import com.kpmbustimingapp.app.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.splashLogo);

        if (logo != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            logo.startAnimation(fadeIn);
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // ✅ SAFE NOW
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        // Disable back on splash
        super.onBackPressed();
    }
}
