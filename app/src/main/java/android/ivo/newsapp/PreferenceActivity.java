package android.ivo.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class PreferenceActivity extends AppCompatActivity {
    public static final int REQUEST_UPDATE_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class PreferenceActivityFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
        @Override

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            preference.setSummary(newValue.toString());
            return true;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.settings, rootKey);
            Preference sortingPreference = findPreference(getString(R.string.menu_item_sort_key));

            bindPreference(sortingPreference);
        }

        private void bindPreference(Preference preference) {
            if (preference != null) {
                preference.setOnPreferenceChangeListener(this);

                // Grab the default values from options_menu.xml
                SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
                String defaultValue = sharedPreferences.getString(preference.getKey(), "");

                // Update the summary
                onPreferenceChange(preference, defaultValue);
            }
        }
    }

}
