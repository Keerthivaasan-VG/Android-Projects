package com.example.certificate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editName1, editName2, editName3;
    Spinner spinnerDept1;
    Button buttonGo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        editName1 = findViewById(R.id.e1); // First Name
        editName2 = findViewById(R.id.e2); // Second Name
        editName3 = findViewById(R.id.e3); // Roll Number
        spinnerDept1 = findViewById(R.id.s1);
        buttonGo1 = findViewById(R.id.b1);

        // Spinner adapter
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.dept_array,
                        android.R.layout.simple_spinner_item
                );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDept1.setAdapter(adapter);

        // Button click
        buttonGo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname = editName1.getText().toString();
                String sname = editName2.getText().toString();
                String rno = editName3.getText().toString();
                String dept = spinnerDept1.getSelectedItem().toString();

                Intent intent = new Intent(MainActivity.this, com.example.certificate.MainActivity2.class);
                intent.putExtra("fname", fname);
                intent.putExtra("sname", sname);
                intent.putExtra("rno", rno);
                intent.putExtra("dept", dept);

                startActivity(intent);
            }
        });
    }
}
