
package com.kimi.elementsss.util;

import android.app.ActionBar;
import android.app.Activity;
import android.support.annotation.NonNull;


public class ActionBarCompat {


    public static void setDisplayHomeAsUpEnabled(@NonNull Activity activity,
                                                 boolean displayHomeAsUp) {
        final ActionBar actionBar = activity.getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(displayHomeAsUp);
        }
    }
}
