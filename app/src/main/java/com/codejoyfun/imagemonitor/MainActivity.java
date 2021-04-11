package com.codejoyfun.imagemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageMonitor.init(new ImageMonitor.Config().debugAction((threadStack, drawableWidth, drawableHeight, imageSize, drawable) -> {

        }).releaseAction((threadStack, drawableWidth, drawableHeight, imageSize, drawable) -> {

        }));

        ImageView imageView = findViewById(R.id.image_view);
        imageView.setImageResource(R.mipmap.ic_launcher);

    }
}