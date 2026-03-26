package com.example.gpslocator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gpslocator.R;


public class MainActivity extends AppCompatActivity {

    EditText etLatitude, etLongitude;
    Button btnOpenMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etLatitude = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        btnOpenMap = findViewById(R.id.btnOpenMap);

        btnOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lat = etLatitude.getText().toString().trim();
                String lon = etLongitude.getText().toString().trim();


                if (lat.isEmpty() || lon.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Please enter both latitude and longitude",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, MainActivity2.class);

                Bundle bundle = new Bundle();
                bundle.putString("latitude", lat);
                bundle.putString("longitude", lon);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}