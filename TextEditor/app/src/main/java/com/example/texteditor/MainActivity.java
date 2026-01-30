package com.example.texteditor;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button b1, b2, b3, b4;
    TextView t1;
    EditText e1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        b1 = findViewById(R.id.button);   // RED
        b2 = findViewById(R.id.button1);  // BLUE
        b3 = findViewById(R.id.button2);  // Increase size
        b4 = findViewById(R.id.button3);  // Decrease size

        t1 = findViewById(R.id.textView);
        e1 = findViewById(R.id.editTextText);

        // Button 1: Set RED text
        b1.setOnClickListener(view -> {
            String text = e1.getText().toString();
            t1.setText(text);
            t1.setTextColor(Color.RED);
            t1.setTypeface(Typeface.SERIF, Typeface.BOLD_ITALIC);
        });

        // Button 2: Set BLUE text
        b2.setOnClickListener(view -> {
            String text = e1.getText().toString();
            t1.setText(text);
            t1.setTextColor(Color.BLUE);
            t1.setTypeface(Typeface.SERIF, Typeface.BOLD_ITALIC);
        });

        // Button 3: Increase Text Size
        b3.setOnClickListener(view -> {
            float currentSize = t1.getTextSize(); // pixels
            float newSize = currentSize + 5;
            if (newSize > 150) newSize = 150;    // max size in pixels
            t1.setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize);
        });

        // Button 4: Decrease Text Size
        b4.setOnClickListener(view -> {
            float currentSize = t1.getTextSize();
            float newSize = currentSize - 5;
            if (newSize < 10) newSize = 10;      // min size in pixels
            t1.setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize);
        });

        // EdgeToEdge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}