package ru.solandme.remindmeabout;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

public class SplashScreen extends Activity {

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
