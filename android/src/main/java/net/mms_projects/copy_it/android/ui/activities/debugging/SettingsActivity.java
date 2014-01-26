package net.mms_projects.copy_it.android.ui.activities.debugging;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import net.mms_projects.copy_it.R;

public class SettingsActivity extends SherlockPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences_debug);
    }
}
