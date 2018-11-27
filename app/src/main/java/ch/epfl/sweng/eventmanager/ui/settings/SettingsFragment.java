package ch.epfl.sweng.eventmanager.ui.settings;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import ch.epfl.sweng.eventmanager.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_preferences,rootKey);
    }
}
