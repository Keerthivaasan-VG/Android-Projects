package com.example.moviebooking;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etMovie, etTime, etTickets;
    Button btnBook, btnEdit, btnDelete;
    TableLayout tableBookings;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMovie = findViewById(R.id.etMovie);
        etTime = findViewById(R.id.etTime);
        etTickets = findViewById(R.id.etTickets);
        btnBook = findViewById(R.id.btnBook);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        tableBookings = findViewById(R.id.tableBookings);

        db = openOrCreateDatabase("MovieDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "show_time TEXT, " +
                "tickets INTEGER)");

        refreshTable();

        btnBook.setOnClickListener(v -> {
            db.execSQL(
                    "INSERT INTO Bookings(title, show_time, tickets) VALUES (?, ?, ?)",
                    new Object[]{
                            etMovie.getText().toString(),
                            etTime.getText().toString(),
                            Integer.parseInt(etTickets.getText().toString())
                    });
            clear();
            refreshTable();
        });

        btnEdit.setOnClickListener(v -> {
            db.execSQL(
                    "UPDATE Bookings SET show_time=?, tickets=? WHERE LOWER(title)=LOWER(?)",
                    new Object[]{
                            etTime.getText().toString(),
                            Integer.parseInt(etTickets.getText().toString()),
                            etMovie.getText().toString()
                    });
            clear();
            refreshTable();
        });

        btnDelete.setOnClickListener(v -> {
            db.execSQL(
                    "DELETE FROM Bookings WHERE LOWER(title)=LOWER(?)",
                    new Object[]{etMovie.getText().toString()}
            );
            clear();
            refreshTable();
        });
    }

    private void refreshTable() {
        tableBookings.removeAllViews();

        // Header row
        TableRow header = new TableRow(this);
        header.addView(makeCell("Movie", true));
        header.addView(makeCell("Time", true));
        header.addView(makeCell("Tickets", true));
        tableBookings.addView(header);

        Cursor c = db.rawQuery(
                "SELECT title, show_time, tickets FROM Bookings",
                null
        );

        while (c.moveToNext()) {
            TableRow row = new TableRow(this);
            row.addView(makeCell(c.getString(0), false));
            row.addView(makeCell(c.getString(1), false));
            row.addView(makeCell(String.valueOf(c.getInt(2)), false));
            tableBookings.addView(row);
        }
        c.close();
    }

    private TextView makeCell(String text, boolean isHeader) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(20, 12, 20, 12);
        tv.setGravity(Gravity.CENTER);
        tv.setBackground(null); // NO borders

        if (isHeader) {
            tv.setTypeface(null, Typeface.BOLD);
        }
        return tv;
    }

    private void clear() {
        etMovie.setText("");
        etTime.setText("");
        etTickets.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}