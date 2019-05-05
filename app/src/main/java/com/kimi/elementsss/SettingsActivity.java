
package com.kimi.elementsss;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.kimi.elementsss.util.ActionBarCompat;
import com.kimi.elementsss.util.PreferenceUtils;
import com.kimi.elementsss.util.SubtextValuesHelper;


public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        SubtextValuesHelper.OnSubtextValuesChangedListener {

    private ListPreference mSubtextValuePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        final boolean darkTheme = PreferenceUtils.getPrefDarkTheme();
        setTheme(darkTheme ? R.style.DarkTheme_Preferences : R.style.LightTheme_Preferences);

        super.onCreate(savedInstanceState);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);
        addPreferencesFromResource(R.xml.preferences);

        mSubtextValuePreference = (ListPreference)findPreference(PreferenceUtils.KEY_SUBTEXT_VALUE);
        final SubtextValuesHelper subtextValuesHelper = new SubtextValuesHelper(this, this);
        mSubtextValuePreference.setEntries(subtextValuesHelper.getList());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(PreferenceUtils.KEY_DARK_THEME.equals(key)) {
            sharedPreferences.edit().commit();

            final Intent intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            Runtime.getRuntime().exit(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSubtextValuesChanged(@NonNull SubtextValuesHelper helper) {
        mSubtextValuePreference.setEntries(helper.getList());
    }
}
