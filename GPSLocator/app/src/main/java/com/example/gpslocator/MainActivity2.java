package com.example.gpslocator;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gpslocator.R;


public class MainActivity2 extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        webView = findViewById(R.id.web_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Toast.makeText(this, "No location data received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String lat = bundle.getString("latitude");
        String lon = bundle.getString("longitude");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String url = "https://www.google.com/maps?q=" + lat + "," + lon;
        webView.loadUrl(url);
    }
}