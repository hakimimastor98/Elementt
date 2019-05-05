
package com.kimi.elementsss.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kimi.elementsss.R;
import com.kimi.elementsss.util.SubtextValuesHelper;

/**
 * Custom Adapter for the block subtext Spinner.
 */
public class BlockSubtextValueListAdapter extends BaseAdapter
        implements SubtextValuesHelper.OnSubtextValuesChangedListener {
    /**
     * The Context
     */
    @NonNull
    private final Context mContext;

    /**
     * The keys of the items
     */
    @NonNull
    private final String[] mKeys;

    /**
     * The SubtextValuesHelper to manage the list of options
     */
    @NonNull
    private final SubtextValuesHelper mHelper;

    /**
     * @param context The Context
     */
    public BlockSubtextValueListAdapter(@NonNull Context context) {
        mContext = context;
        mKeys = context.getResources().getStringArray(R.array.subtextValues);
        mHelper = new SubtextValuesHelper(context, this);
    }

    @Override
    public int getCount() {
        return mKeys.length;
    }

    @Override
    public String getItem(int i) {
        return mKeys[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Get the index of an item based on its key.
     *
     * @param key The key
     * @return The item index
     */
    public int getItemIndex(@NonNull String key) {
        for(int i = 0; i < mKeys.length; i++) {
            if(mKeys[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent,
                android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent, android.R.layout.simple_spinner_item);
    }

    /**
     * Create the view for an item.
     *
     * @param position    The item index
     * @param convertView The previous View to recycle
     * @param parent      The parent ViewGroup
     * @param layout      The layout ID to inflate
     * @return The populated View
     */
    @NonNull
    private View createView(int position, @Nullable View convertView, ViewGroup parent,
                            int layout) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layout, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.text = convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        }
        ((ViewHolder)convertView.getTag()).text.setText(mHelper.getItem(position));
        return convertView;
    }

    @Override
    public void onSubtextValuesChanged(@NonNull SubtextValuesHelper helper) {
        notifyDataSetChanged();
    }

    /**
     * Caches references to Views within a layout.
     */
    private static class ViewHolder {
        /**
         * The primary text area
         */
        TextView text;
    }
}
