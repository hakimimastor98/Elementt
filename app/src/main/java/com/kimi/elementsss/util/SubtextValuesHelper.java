
package com.kimi.elementsss.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kimi.elementsss.R;

import java.util.Arrays;


public class SubtextValuesHelper implements SharedPreferences.OnSharedPreferenceChangeListener {

    public interface OnSubtextValuesChangedListener {

        void onSubtextValuesChanged(@NonNull SubtextValuesHelper helper);
    }


    @NonNull
    private final String[] mList;


    @Nullable
    private final OnSubtextValuesChangedListener mListener;

    public SubtextValuesHelper(@NonNull Context context,
                               @Nullable OnSubtextValuesChangedListener listener) {
        mList = context.getResources().getStringArray(R.array.subtextValueNames);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);

        updateTempUnit();
        mListener = listener;
    }

    @NonNull
    public String[] getList() {
        return Arrays.copyOf(mList, mList.length);
    }

    @Nullable
    public String getItem(int index) {
        if(index >= 0 && index < mList.length) {
            return mList[index];
        }
        return null;
    }

    private void updateTempUnit() {
        final String unit;
        switch(PreferenceUtils.getPrefTempUnit()) {
            case PreferenceUtils.TEMP_C:
                unit = "℃";
                break;
            case PreferenceUtils.TEMP_F:
                unit = "℉";
                break;
            default:
                unit = "K";
        }
        mList[2] = mList[2].substring(0, mList[2].length() - 2) + unit + ")";
        mList[3] = mList[3].substring(0, mList[3].length() - 2) + unit + ")";
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(PreferenceUtils.KEY_TEMP_UNITS.equals(key)) {
            updateTempUnit();
            if(mListener != null) {
                mListener.onSubtextValuesChanged(this);
            }
        }
    }
}
