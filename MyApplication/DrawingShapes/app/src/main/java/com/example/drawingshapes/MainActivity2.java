package com.example.drawingshapes;

import android.graphics.*;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageView imageView = findViewById(R.id.imageView);

        String shape = getIntent().getStringExtra("shape");
        String color = getIntent().getStringExtra("color");

        if (shape == null || color == null) return;

        Bitmap bitmap = Bitmap.createBitmap(720, 1280, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setColor(getColor(color));
        paint.setTextSize(40);

        canvas.drawText(shape, 250, 150, paint);
        drawShape(canvas, paint, shape);

        imageView.setImageBitmap(bitmap);
    }

    private void drawShape(Canvas canvas, Paint paint, String shape) {
        switch (shape) {
            case "Rectangle":
                canvas.drawRect(200, 200, 600, 500, paint);
                break;

            case "Square":
                canvas.drawRect(250, 200, 550, 500, paint);
                break;

            case "House":
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);

                // House body (rectangle)
                canvas.drawRect(250, 350, 550, 650, paint);

                // Roof (triangle)
                Path roof = new Path();
                roof.moveTo(250, 350);   // left top of house
                roof.lineTo(400, 200);   // roof peak
                roof.lineTo(550, 350);   // right top of house
                roof.close();
                canvas.drawPath(roof, paint);

                // Door
                canvas.drawRect(370, 480, 430, 650, paint);

                // Left window
                canvas.drawRect(280, 400, 340, 460, paint);

                // Right window
                canvas.drawRect(460, 400, 520, 460, paint);
                break;

            case "Triangle":
                Path path = new Path();
                path.moveTo(400, 200);
                path.lineTo(200, 500);
                path.lineTo(600, 500);
                path.close();
                canvas.drawPath(path, paint);
                break;
        }
    }

    private int getColor(String color) {
        switch (color) {
            case "Red": return Color.RED;
            case "Blue": return Color.BLUE;
            case "Green": return Color.GREEN;
            case "Yellow": return Color.YELLOW;
            default: return Color.BLACK;
        }
    }
}

