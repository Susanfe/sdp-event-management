package ch.epfl.sweng.eventmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.testfairy.TestFairy;

import ch.epfl.sweng.eventmanager.ui.event.selection.EventPickingActivity;

/**
 * Create a splash screen while loading main components.
 * see https://www.bignerdranch.com/blog/splash-screens-the-right-way/
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TestFairy.begin(this, "b89410efc8c54441f0cdf912940d4c0bd61837f9");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = new Intent(this, EventPickingActivity.class);
        startActivity(intent);
        finish();

        //TODO Cache events & images for eventPicking
    }
}
