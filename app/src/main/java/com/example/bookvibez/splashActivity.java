package com.example.bookvibez;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * The splash screen that loads when pressing the app icon. connects to the MainActivity (needs to
 * changed to LoginActivity when conected).
 */
public class splashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

//                startActivity(new Intent(splashActivity.this, MainActivity.class));
                finish();
                Intent intent = new Intent(splashActivity.this, MainActivity.class);
                // todo: needs to be chanced to LoginActivity instead of MainActivity.
                splashActivity.this.startActivity(intent);
            }
        },2000); // the time delay for the splash screen
    }

}
