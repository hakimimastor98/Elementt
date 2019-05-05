
package com.kimi.elementsss.provider;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Element {
    /**
     * The element number
     */
    public final int number;

    /**
     * The element symbol
     */
    @NonNull
    public final String symbol;

    /**
     * The group
     */
    public final int group;

    /**
     * The period
     */
    public final int period;

    /**
     * The block
     */
    public final char block;

    /**
     * The atomic weight
     */
    public final double weight;

    /**
     * The density in g/cm³
     */
    @Nullable
    public final Double density;

    /**
     * The melting point in K
     */
    @Nullable
    public final Double melt;

    /**
     * The boiling point in K
     */
    @Nullable
    public final Double boil;

    /**
     * The specific heat in J/g·K
     */
    @Nullable
    public final Double heat;

    /**
     * The electronegativity on the Pauling scale
     */
    @Nullable
    public final Double negativity;

    /**
     * The abundance in mg/kg
     */
    @Nullable
    public final Double abundance;

    /**
     * The category
     */
    public final int category;

    /**
     * The electron configuration
     */
    @NonNull
    public final Configuration configuration;

    /**
     * The number of electrons per shell
     */
    @NonNull
    public final Integer[] electrons;

    /**
     * Whether the element is unstable
     */
    public final boolean unstable;


    Element(int number, @NonNull String symbol, int group, int period, char block, double weight,
            @Nullable Double density, @Nullable Double melt, @Nullable Double boil,
            @Nullable Double heat, @Nullable Double negativity, @Nullable Double abundance,
            int category, @NonNull Configuration configuration, @NonNull Integer[] electrons,
            boolean unstable) {
        this.number = number;
        this.symbol = symbol;
        this.group = group;
        this.period = period;
        this.block = block;
        this.weight = weight;
        this.density = density;
        this.melt = melt;
        this.boil = boil;
        this.heat = heat;
        this.negativity = negativity;
        this.abundance = abundance;
        this.category = category;
        this.configuration = configuration;
        this.electrons = electrons;
        this.unstable = unstable;
    }


    public static class Configuration {

        @Nullable
        public final String baseElement;

        /**
         * The list of orbitals
         */
        @NonNull
        public final Orbital[] orbitals;

        /**
         * @param baseElement The symbol of the base element that this configuration is built upon,
         *                    if any
         * @param orbitals    The list of orbitals
         */
        Configuration(@Nullable String baseElement, @NonNull Orbital[] orbitals) {
            this.baseElement = baseElement;
            this.orbitals = orbitals;
        }
    }

    /**
     * Represents an orbital in an electron configuration.
     */
    public static class Orbital {
        /**
         * The shell number
         */
        public final int shell;

        /**
         * The orbital type
         */
        public final char orbital;

        /**
         * The number of electrons in this orbital
         */
        public final int electrons;

        /**
         * @param shell     The shell number
         * @param orbital   The orbital type
         * @param electrons The number of electrons in this orbital
         */
        Orbital(int shell, char orbital, int electrons) {
            this.shell = shell;
            this.orbital = orbital;
            this.electrons = electrons;
        }
    }
}
