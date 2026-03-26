package com.example.drawingshapes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner shapeSpinner = findViewById(R.id.spinner);
        Spinner colorSpinner = findViewById(R.id.spinner2);
        Button drawButton = findViewById(R.id.button);

        // Shape adapter
        shapeSpinner.setAdapter(
                ArrayAdapter.createFromResource(
                        this,
                        R.array.spinner,
                        android.R.layout.simple_spinner_dropdown_item
                )
        );

        // Color adapter
        colorSpinner.setAdapter(
                ArrayAdapter.createFromResource(
                        this,
                        R.array.colors,
                        android.R.layout.simple_spinner_dropdown_item
                )
        );

        drawButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("shape", shapeSpinner.getSelectedItem().toString());
            intent.putExtra("color", colorSpinner.getSelectedItem().toString());
            startActivity(intent);
        });
    }
}