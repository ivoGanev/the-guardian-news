package android.ivo.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class PreferenceActivity extends AppCompatActivity {
    public static final int REQUEST_UPDATE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class PreferenceActivityFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        @Override
        // Every time a preference gets changed this method will be called back
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // setSummary shows a brief summary text in the UI underneath the preference which explains the current
            // input value
            preference.setSummary(newValue.toString());
            return true;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            // inflate the settings
            setPreferencesFromResource(R.xml.settings, rootKey);

            // locate the preference
            Preference sortingPreference = findPreference(getString(R.string.menu_item_sort_key));

            // we need to set a default value for the list
            bindPreference(sortingPreference);
        }

        private void bindPreference(Preference preference) {
            if (preference != null) {
                preference.setOnPreferenceChangeListener(this);

                // Grab the default values from options_menu.xml
                SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
                String defaultValue = sharedPreferences.getString(preference.getKey(), "");

                // Call on preference change to update the summary
                onPreferenceChange(preference, defaultValue);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Request update from the main activity when the back button is pressed.
        Intent i = new Intent(this, MainActivity.class);
        startActivityForResult(i, REQUEST_UPDATE);
    }
}
