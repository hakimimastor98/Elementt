
package com.kimi.elementsss.util;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.kimi.elementsss.R;

import com.kimi.elementsss.SettingsActivity;


public class CommonMenuHandler {

    public static boolean handleSelect(@NonNull FragmentActivity activity, int id) {
        switch(id) {
            case R.id.menu_settings:
                activity.startActivity(new Intent(activity, SettingsActivity.class));
                return true;


        }
        return true;
    }
}
