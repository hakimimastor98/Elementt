
package com.kimi.elementsss.widget;

import android.support.annotation.NonNull;

import com.kimi.elementsss.provider.Element;

/**
 * Stores data for a single block on a PeriodicTableView.
 */
public class PeriodicTableBlock {
    /**
     * The Element
     */
    @NonNull
    public final Element element;

    /**
     * Text to display below the symbol
     */
    public String subtext;

    /**
     * Block background color
     */
    public int color = 0xFFCCCCCC;

    /**
     * Grid position
     */
    int row;
    int col;

    /**
     * @param element The Element
     */
    public PeriodicTableBlock(@NonNull Element element) {
        this.element = element;
    }
}
