
package com.kimi.elementsss.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.kimi.elementsss.R;

import com.kimi.elementsss.util.ElementUtils;
import com.kimi.elementsss.util.PreferenceUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * Renders a color legend on a PeriodicTableView.
 */
class PeriodicTableLegend {
    /**
     * Map of key values to labels
     */
    @NonNull
    private final HashMap<String, String> mMap = new LinkedHashMap<>();

    /**
     * Paint used to draw backgrounds
     */
    @NonNull
    private final Paint mPaint = new Paint();

    /**
     * Paint used to draw text
     */
    @NonNull
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * Rectangle used to draw backgrounds
     */
    @NonNull
    private final Rect mRect = new Rect();

    /**
     * @param context The Context
     */
    PeriodicTableLegend(@NonNull Context context) {
        invalidate(context);
    }

    /**
     * Load the legend data from resources.
     *
     * @param context The Context
     */
    void invalidate(@NonNull Context context) {
        final Resources res = context.getResources();
        final String[] keys;
        final String[] nameValues;
        if(PreferenceUtils.COLOR_BLOCK.equals(PreferenceUtils.getPrefElementColors())) {
            keys = res.getStringArray(R.array.ptBlocks);
            nameValues = res.getStringArray(R.array.ptBlocks);
        } else {
            keys = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
            nameValues = res.getStringArray(R.array.ptCategories);
        }

        mMap.clear();
        for(int i = 0; i < keys.length; i++) {
            mMap.put(keys[i], nameValues[i]);
        }
    }

    /**
     * Render the legend within the specified rectangle on the specified Canvas. The legend appears
     * as a grid of colored rectangles in 4 rows and a variable number of columns. Each rectangle
     * contains text declaring the value represented by the rectangle's color.
     *
     * @param canvas Canvas on which to draw
     * @param rect   Boundaries within which to draw
     */
    void drawLegend(@NonNull Canvas canvas, @NonNull Rect rect) {
        final int count = mMap.size();
        final int rows = 4;
        final int cols = (int)Math.ceil(count / (double)rows);
        final int boxHeight = (rect.bottom - rect.top) / rows;

        mTextPaint.setTextSize(boxHeight / 2);

        int boxWidth = 0;
        if(cols < 2) {
            boxWidth = rect.width();
        } else {
            for(String value : mMap.values()) {
                boxWidth = (int)Math.ceil(Math.max(boxWidth, mTextPaint.measureText(value)));
            }
            boxWidth += boxWidth / 10;
        }

        final float totalWidth = boxWidth * cols;
        if(totalWidth > rect.width()) {
            boxWidth *= rect.width() / totalWidth;
            mTextPaint.setTextSize(mTextPaint.getTextSize() * rect.width() / totalWidth);
        } else {
            rect.left += (rect.width() - totalWidth) / 2;
        }

        int n = 0;
        for(Entry<String, String> entry : mMap.entrySet()) {
            mRect.top = rect.top + n % rows * boxHeight + 1;
            mRect.left = rect.left + n / rows * boxWidth + 1;
            mRect.bottom = mRect.top + boxHeight - 1;
            mRect.right = mRect.left + boxWidth - 1;

            mPaint.setColor(ElementUtils.getKeyColor(entry.getKey()));
            canvas.drawRect(mRect, mPaint);

            canvas.drawText(entry.getValue(), mRect.left + boxWidth / 20,
                    mRect.bottom - boxHeight / 2 + mTextPaint.getTextSize() / 2, mTextPaint);

            n++;
        }
    }
}
