package ch.epfl.sweng.eventmanager.ui.credits;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.sweng.eventmanager.R;

public class CreditsActivity extends AppCompatActivity {

    public static final String SELECTED_EVENT_ID = "ch.epfl.sweng.CREDITS_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }
}
