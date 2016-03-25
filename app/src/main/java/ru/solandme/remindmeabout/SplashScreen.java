package ru.solandme.remindmeabout;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent goToMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goToMain);
                finish();
            }
        }, 2000);
    }
}
