package com.codejoyfun.imagemonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageMonitor.init(new ImageMonitor.Config().dealWarning((threadStack, drawableWidth, drawableHeight, imageSize, drawable) -> {
            if(BuildConfig.DEBUG){
                //debug环境下，建议弹框
            }else{
                //正式环境，建议上报friebase
            }
        }));

        ImageView imageView = findViewById(R.id.image_view);
        imageView.setImageResource(R.mipmap.ic_launcher);

    }
}