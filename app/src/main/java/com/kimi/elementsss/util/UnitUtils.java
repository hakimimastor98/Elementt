
package com.kimi.elementsss.util;

import android.support.annotation.NonNull;


public class UnitUtils {

    @NonNull
    public static Double KtoC(@NonNull Double k) {
        return k - 273.15;
    }


    @NonNull
    public static Double KtoF(@NonNull Double k) {
        return k * 9.0 / 5 - 459.67;
    }
}
