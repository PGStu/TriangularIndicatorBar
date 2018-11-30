package com.example.pg.triangularindicatorbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pg.triangularindicatorbar.view.TriangularIndicatorBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       TriangularIndicatorBar triangularIndicatorBar = (TriangularIndicatorBar)findViewById(R.id.triangularIndicatorBar);
        triangularIndicatorBar.setScore(360);
    }
}
