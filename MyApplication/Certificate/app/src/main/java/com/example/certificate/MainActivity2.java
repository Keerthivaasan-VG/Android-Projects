package com.example.certificate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.certificate.R;

public class MainActivity2 extends AppCompatActivity {

    TextView textViewResult;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textViewResult = findViewById(R.id.textView2);

        // Receive data
        String fname = getIntent().getStringExtra("fname");
        String sname = getIntent().getStringExtra("sname");
        String rno = getIntent().getStringExtra("rno");
        String dept = getIntent().getStringExtra("dept");

        // Certificate text
        String result =
                "This is to certify that Mr./Ms " + fname + " " + sname +
                        " (" + rno + ") from the department of " + dept +
                        " has successfully completed the prescribed course titled " +
                        "MOBILE APPLICATION DEVELOPMENT. The course was completed during the year 2026. " +
                        "His/Her performance and conduct during the course were found to be satisfactory. " +
                        "This certificate is issued upon successful completion of the course requirements.";

        textViewResult.setText(result);
    }
}
