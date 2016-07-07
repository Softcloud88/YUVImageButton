package com.softcloud.clickableimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupYUVButton();
    }

    private void setupYUVButton() {
        YUVImageButton imgKobe = (YUVImageButton) findViewById(R.id.img_kobe);
        YUVImageButton imgTifa = (YUVImageButton) findViewById(R.id.img_tifa);
        if (imgKobe == null || imgTifa == null) {
            return;
        }

        imgKobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "kobe clicked",Toast.LENGTH_SHORT).show();
            }
        });
        imgTifa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "tifa clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
