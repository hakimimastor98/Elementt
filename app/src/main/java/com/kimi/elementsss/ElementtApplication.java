
package com.kimi.elementsss;

import android.app.Application;

import com.kimi.elementsss.util.ElementUtils;
import com.kimi.elementsss.util.PreferenceUtils;

public class ElementtApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.setup(this);
        ElementUtils.setup(this);
    }
}
