package com.kimi.elementsss;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.ZoomControls;

import com.kimi.elementsss.provider.Element;
import com.kimi.elementsss.provider.Elements;
import com.kimi.elementsss.util.CommonMenuHandler;
import com.kimi.elementsss.util.PreferenceUtils;
import com.kimi.elementsss.util.UnitUtils;
import com.kimi.elementsss.widget.BlockSubtextValueListAdapter;
import com.kimi.elementsss.widget.PeriodicTableBlock;
import com.kimi.elementsss.widget.PeriodicTableView;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class TableActivityy extends FragmentActivity
        implements OnSharedPreferenceChangeListener {

    private static final long IMMERSIVE_MODE_DELAY = 4000;


    private static final long ZOOM_BUTTON_DELAY = 5000;


    @NonNull
    private final Handler mHandler = new Handler();


    private Runnable mImmersiveModeCallback;

    private PeriodicTableView mPeriodicTableView;

    private View mControlBar;

    private ZoomControls mZoomControls;

    private Spinner mSpinnerSubtextValue;

    private Spinner mSpinnerBlockColors;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    static {
        DECIMAL_FORMAT.setMaximumFractionDigits(4);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        final boolean darkTheme = PreferenceUtils.getPrefDarkTheme();
        setTheme(darkTheme ? R.style.DarkTheme : R.style.LightTheme);

        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        setupImmersiveMode();
        setContentView(R.layout.activity_periodic_table);

        mPeriodicTableView = findViewById(R.id.ptview);
        mPeriodicTableView.setPeriodicTableListener(new PeriodicTableView.PeriodicTableListener() {
            @Override
            public void onItemClick(@NonNull PeriodicTableBlock item) {
                ElementDetailsFragment.showDialog(getSupportFragmentManager(), item.element.number);
            }

            @Override
            public void onZoomEnd(@NonNull PeriodicTableView periodicTableView) {
                mZoomControls.setIsZoomInEnabled(periodicTableView.canZoomIn());
                mZoomControls.setIsZoomOutEnabled(periodicTableView.canZoomOut());
            }
        });

        mControlBar = findViewById(R.id.controls);
        if(PreferenceUtils.getPrefShowControls()) {
            mControlBar.setVisibility(View.VISIBLE);
        }

        setupZoomControls();
        setupSubtextValueSpinner();
        setupBlockColorSpinner();

        loadElements();
    }

    private void setupZoomControls() {
        final Runnable hideZoom = new Runnable() {
            @Override
            public void run() {
                mZoomControls.hide();
                mZoomControls.setVisibility(View.INVISIBLE);
            }
        };

        mZoomControls = findViewById(R.id.zoom);
        mZoomControls.setVisibility(View.INVISIBLE);
        mZoomControls.setIsZoomOutEnabled(false);
        mZoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(hideZoom);
                mPeriodicTableView.zoomIn();
                mZoomControls.setIsZoomOutEnabled(true);
                mHandler.postDelayed(hideZoom, ZOOM_BUTTON_DELAY);
            }
        });
        mZoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(hideZoom);
                mPeriodicTableView.zoomOut();
                mZoomControls.setIsZoomInEnabled(true);
                mHandler.postDelayed(hideZoom, ZOOM_BUTTON_DELAY);
            }
        });

        findViewById(R.id.zoomButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(hideZoom);
                if(mZoomControls.getVisibility() == View.INVISIBLE) {
                    mZoomControls.show();
                    mZoomControls.setVisibility(View.VISIBLE);
                }
                mHandler.postDelayed(hideZoom, ZOOM_BUTTON_DELAY);
            }
        });
    }

    private void setupSubtextValueSpinner() {
        mSpinnerSubtextValue = findViewById(R.id.subtextValue);
        final BlockSubtextValueListAdapter adapter = new BlockSubtextValueListAdapter(this);
        mSpinnerSubtextValue.setAdapter(adapter);
        mSpinnerSubtextValue.setSelection(adapter
                .getItemIndex(PreferenceUtils.getPrefSubtextValue()));
        mSpinnerSubtextValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PreferenceUtils.setPrefSubtextValue(adapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setupBlockColorSpinner() {
        mSpinnerBlockColors = findViewById(R.id.blockColors);
        if(mSpinnerBlockColors == null) {
            return;
        }

        final String pref = PreferenceUtils.getPrefElementColors();
        mSpinnerBlockColors.setSelection(pref.equals(PreferenceUtils.COLOR_CAT) ? 0 : 1);
        mSpinnerBlockColors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PreferenceUtils.setPrefElementColors(i == 0 ? PreferenceUtils.COLOR_CAT
                        : PreferenceUtils.COLOR_BLOCK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mHandler.removeCallbacks(mImmersiveModeCallback);
            hideSystemUi();
        }
    }

    private void setupImmersiveMode() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mImmersiveModeCallback = new Runnable() {
                @Override
                public void run() {
                    hideSystemUi();
                }
            };

            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            );
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                    new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            mHandler.removeCallbacks(mImmersiveModeCallback);
                            if((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
                                mHandler.postDelayed(mImmersiveModeCallback, IMMERSIVE_MODE_DELAY);
                            }
                        }
                    });

            final ActionBar actionBar = getActionBar();
            if(actionBar != null) {
                actionBar.addOnMenuVisibilityListener(new ActionBar.OnMenuVisibilityListener() {
                    @Override
                    public void onMenuVisibilityChanged(boolean isVisible) {
                        if(isVisible) {
                            mHandler.removeCallbacks(mImmersiveModeCallback);
                        } else {
                            mHandler.postDelayed(mImmersiveModeCallback, IMMERSIVE_MODE_DELAY);
                        }
                    }
                });
            }
        }
    }

    private void hideSystemUi() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
            );

            findViewById(R.id.touchFrame).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.periodic_table, menu);
        inflater.inflate(R.menu.common, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch(id) {
            case R.id.menu_list:
                startActivity(new Intent(this, ElementListActivity.class));
                return true;
        }
        return CommonMenuHandler.handleSelect(this, id) || super.onOptionsItemSelected(item);
    }

    @NonNull
    private String getSubtext(@NonNull Element element) {
        switch(PreferenceUtils.getPrefSubtextValue()) {
            case PreferenceUtils.SUBTEXT_WEIGHT:
                if(element.unstable) {
                    return "[" + (int)element.weight + "]";
                } else {
                    return DECIMAL_FORMAT.format(element.weight);
                }
            case PreferenceUtils.SUBTEXT_MELT:
            case PreferenceUtils.SUBTEXT_BOIL:
                Double value;
                if(PreferenceUtils.SUBTEXT_MELT.equals(PreferenceUtils.getPrefSubtextValue())) {
                    value = element.melt;
                } else {
                    value = element.boil;
                }
                if(value != null) {
                    switch(PreferenceUtils.getPrefTempUnit()) {
                        case PreferenceUtils.TEMP_C:
                            value = UnitUtils.KtoC(value);
                            break;
                        case PreferenceUtils.TEMP_F:
                            value = UnitUtils.KtoF(value);
                            break;
                    }
                    return DECIMAL_FORMAT.format(value);
                }
                break;
            case PreferenceUtils.SUBTEXT_DENSITY:
                if(element.density != null) {
                    if(element.density < 0.0001) {
                        return "<0.0001";
                    }
                    return DECIMAL_FORMAT.format(element.density);
                }
                break;
            case PreferenceUtils.SUBTEXT_ABUNDANCE:
                if(element.abundance != null) {
                    if(element.abundance < 0.001) {
                        return "<0.001";
                    }
                    return DECIMAL_FORMAT.format(element.abundance);
                }
                break;
            case PreferenceUtils.SUBTEXT_HEAT:
                if(element.heat != null) {
                    return String.valueOf(element.heat);
                }
                break;
            case PreferenceUtils.SUBTEXT_NEGATIVITY:
                if(element.negativity != null) {
                    return String.valueOf(element.negativity);
                }
                break;
        }
        return "?";
    }

    private void loadElements() {
        final ArrayList<PeriodicTableBlock> periodicTableBlocks = new ArrayList<>();
        PeriodicTableBlock block;
        for(Element element : Elements.getElements()) {
            block = new PeriodicTableBlock(element);
            block.subtext = getSubtext(element);

            periodicTableBlocks.add(block);
        }

        mPeriodicTableView.setBlocks(periodicTableBlocks);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch(key) {
            case PreferenceUtils.KEY_SHOW_CONTROLS:
                mControlBar.setVisibility(PreferenceUtils.getPrefShowControls() ? View.VISIBLE
                        : View.GONE);
                break;
            case PreferenceUtils.KEY_ELEMENT_COLORS:
                loadElements();
                if(PreferenceUtils.COLOR_BLOCK.equals(PreferenceUtils.getPrefElementColors())) {
                    mSpinnerBlockColors.setSelection(1);
                } else {
                    mSpinnerBlockColors.setSelection(0);
                }
                mPeriodicTableView.invalidateLegend();
                break;
            case PreferenceUtils.KEY_SUBTEXT_VALUE:
                loadElements();
                mSpinnerSubtextValue.setSelection(
                        ((BlockSubtextValueListAdapter)mSpinnerSubtextValue.getAdapter())
                                .getItemIndex(PreferenceUtils.getPrefSubtextValue()));
        }
    }
}













