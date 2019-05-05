
package com.kimi.elementsss;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.kimi.elementsss.util.PreferenceUtils;
import com.kimi.elementsss.widget.ElementListAdapter;


public class ElementListFragment extends ListFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String KEY_SORT = "key_sort";
    private static final String KEY_SORT_REVERSE = "key_sort_reverse";
    private static final String KEY_FILTER = "key_filter";
    private static final String KEY_ACTIVATED_ITEM = "key_activated_item";


    private ElementListAdapter mAdapter;


    private String mFilter;


    private int mSort = ElementListAdapter.SORT_NUMBER;


    private boolean mSortReverse = false;


    private long mActivatedItem = -1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Context context = getContext();
        if(context == null) {
            return;
        }

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.registerOnSharedPreferenceChangeListener(this);

        if(savedInstanceState != null) {
            mSort = savedInstanceState.getInt(KEY_SORT, mSort);
            mSortReverse = savedInstanceState.getBoolean(KEY_SORT_REVERSE, mSortReverse);
            mFilter = savedInstanceState.getString(KEY_FILTER);
            mActivatedItem = savedInstanceState.getLong(KEY_ACTIVATED_ITEM, mActivatedItem);
        }

        mAdapter = new ElementListAdapter(context);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setActivatedPosition(mAdapter.getItemPosition(mActivatedItem));
            }
        });
        mAdapter.getFilter().filter(mFilter);
        mAdapter.setSort(mSort, mSortReverse);
        setListAdapter(mAdapter);

        setupFilter();
        setupSort();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SORT, mSort);
        outState.putBoolean(KEY_SORT_REVERSE, mSortReverse);
        outState.putString(KEY_FILTER, mFilter);
        outState.putLong(KEY_ACTIVATED_ITEM, mActivatedItem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        mActivatedItem = id;

        final ElementListActivity activity = (ElementListActivity)getActivity();
        if(activity != null) {
            activity.onItemSelected((int)id);
        }
    }


    private void setupFilter() {
        final Activity activity = getActivity();
        if(activity == null) {
            return;
        }

        final EditText filterEditText = activity.findViewById(R.id.filter);
        filterEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                mFilter = s.toString();
                mAdapter.getFilter().filter(mFilter);
            }
        });
    }

    private void setupSort() {
        final Activity activity = getActivity();
        if(activity == null) {
            return;
        }

        final Button sortButton = activity.findViewById(R.id.sort);
        sortButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openSortDialog();
            }
        });
    }

    private void openSortDialog() {
        final FragmentManager fm = getFragmentManager();
        if(fm == null) {
            return;
        }

        final DialogFragment fragment = new SortDialog();
        fragment.setTargetFragment(this, 0);
        fragment.show(fm, null);
    }

    private void setSort(int field) {
        mSortReverse = field == mSort && !mSortReverse;
        mSort = field;
        mAdapter.setSort(mSort, mSortReverse);
    }


    @SuppressWarnings("SameParameterValue")
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }


    private void setActivatedPosition(int position) {
        if(position != ListView.INVALID_POSITION) {
            getListView().setItemChecked(position, true);
        } else {
            getListView().setItemChecked(getListView().getCheckedItemPosition(), false);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(PreferenceUtils.KEY_ELEMENT_COLORS.equals(key)) {
            mAdapter.notifyDataSetInvalidated();
        }
    }


    public static class SortDialog extends DialogFragment {
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.titleSort)
                    .setItems(R.array.sortFieldNames, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            final ElementListFragment fragment =
                                    (ElementListFragment)getTargetFragment();
                            if(fragment != null) {
                                fragment.setSort(item);
                            }

                            dialog.dismiss();
                        }
                    })
                    .create();
        }

    }
}
