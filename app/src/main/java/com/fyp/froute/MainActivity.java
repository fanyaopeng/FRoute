package com.fyp.froute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fyp.routeannotation.Route;
import com.fyp.routeapi.RouteUtils;

@Route("mainActivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
