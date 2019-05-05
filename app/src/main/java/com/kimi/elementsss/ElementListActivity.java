
package com.kimi.elementsss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kimi.elementsss.util.ActionBarCompat;
import com.kimi.elementsss.util.CommonMenuHandler;
import com.kimi.elementsss.util.PreferenceUtils;


public class ElementListActivity extends FragmentActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final boolean darkTheme = PreferenceUtils.getPrefDarkTheme();
        setTheme(darkTheme ? R.style.DarkTheme : R.style.LightTheme);

        super.onCreate(savedInstanceState);

        ActionBarCompat.setDisplayHomeAsUpEnabled(this, true);

        setContentView(R.layout.activity_element_list);

        if(findViewById(R.id.elementDetails) != null) {
            mTwoPane = true;
            final ElementListFragment fragment = (ElementListFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.elementList);
            if(fragment != null) {
                fragment.setActivateOnItemClick(true);
            }
        }
    }

    public void onItemSelected(int id) {
        if(mTwoPane) {
            final Fragment fragment = ElementDetailsFragment.getInstance(id);
            getSupportFragmentManager().beginTransaction().replace(R.id.elementDetails, fragment)
                    .commit();
        } else {
            final Intent intent = new Intent(this, ElementDetailsActivity.class);
            intent.putExtra(ElementDetailsActivity.EXTRA_ATOMIC_NUMBER, id);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.element_list, menu);
        inflater.inflate(R.menu.common, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
            case R.id.menu_table:
                finish();
                return true;
        }
        return CommonMenuHandler.handleSelect(this, id) || super.onOptionsItemSelected(item);
    }
}
