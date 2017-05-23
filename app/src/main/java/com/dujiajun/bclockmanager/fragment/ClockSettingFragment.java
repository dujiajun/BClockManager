package com.dujiajun.bclockmanager.fragment;

import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.dujiajun.bclockmanager.R;

/**
 * Created by cqduj on 2017/05/18.
 */

public class ClockSettingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    private MultiSelectListPreference pref_frequency;
    private SwitchPreference pref_vibrate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_clock_setting);
        pref_frequency = (MultiSelectListPreference) findPreference("frequency");
        //String[] weekdays = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
        //pref_frequency.setEntries(weekdays);
        //pref_frequency.setEntryValues(weekdays);
        pref_vibrate = (SwitchPreference) findPreference("vibrate");

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        return false;
    }
}
