package ch.epfl.sweng.eventmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import ch.epfl.sweng.eventmanager.ui.eventSelector.EventPickingActivity;

/**
 * Create a splash screen while loading main components.
 * see https://www.bignerdranch.com/blog/splash-screens-the-right-way/
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, EventPickingActivity.class);
        startActivity(intent);
        finish();

        //TODO Cache events & images for eventPicking
    }
}
