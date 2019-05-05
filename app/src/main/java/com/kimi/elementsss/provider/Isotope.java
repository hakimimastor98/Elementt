
package com.kimi.elementsss.provider;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;

import java.util.Locale;


public class Isotope {
    /**
     * The mass number of the isotope
     */
    private final int number;

    /**
     * The symbol for the isotope
     */
    @NonNull
    private final String symbol;

    /**
     * The relative atomic mass of the isotope
     */
    public final double mass;

    Isotope(int number, @NonNull String symbol, double mass) {
        this.number = number;
        this.symbol = symbol;
        this.mass = mass;
    }

    /**
     * Get the symbol representing the isotope, which is the mass number in superscript followed by
     * the symbol.
     *
     * @return The symbol representing the isotope
     */
    @NonNull
    public Spanned getSymbol() {
        final String html =
                String.format(Locale.US, "<sup><small>%d</small></sup>%s", number, symbol);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, 0);
        }
        //noinspection deprecation
        return Html.fromHtml(html);
    }
}
