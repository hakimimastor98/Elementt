
package com.kimi.elementsss;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.kimi.elementsss.provider.Element;
import com.kimi.elementsss.provider.Elements;
import com.kimi.elementsss.provider.Isotope;
import com.kimi.elementsss.provider.Isotopes;
import com.kimi.elementsss.util.ElementUtils;
import com.kimi.elementsss.util.PreferenceUtils;
import com.kimi.elementsss.util.UnitUtils;

import java.text.DecimalFormat;
import java.util.Locale;

public class ElementDetailsFragment extends DialogFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "ElementDetailsFragment";


    private static final String ARG_ATOMIC_NUMBER = "atomic_number";
    private static final String ARG_ATOMIC_SYMBOL = "atomic_symbol";

    private TextView mTxtHeader;

    private RelativeLayout mElementBlock;
    private TextView mTxtElementSymbol;
    private TextView mTxtElementNumber;
    private TextView mTxtElementWeight;
    private TextView mTxtElementElectrons;


    private TextView mTxtNumber;
    private TextView mTxtSymbol;
    private TextView mTxtName;
    private TextView mTxtWeight;
    private TextView mTxtConfiguration;
    private TextView mTxtElectrons;
    private TextView mTxtCategory;
    private TextView mTxtGPB;
    private TextView mTxtDensity;
    private TextView mTxtMelt;
    private TextView mTxtBoil;
    private TextView mTxtHeat;
    private TextView mTxtNegativity;
    private TextView mTxtAbundance;


    private TableLayout mIsoTable;


    private String mStringUnknown;


    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();

    static {
        DECIMAL_FORMAT.setMaximumFractionDigits(8);
    }

    private Element mElement;


    @NonNull
    public static DialogFragment getInstance(int atomicNumber) {
        final DialogFragment fragment = new ElementDetailsFragment();

        final Bundle args = new Bundle();
        args.putInt(ARG_ATOMIC_NUMBER, atomicNumber);
        fragment.setArguments(args);

        return fragment;
    }


    @NonNull
    public static DialogFragment getInstance(@NonNull String atomicSymbol) {
        final DialogFragment fragment = new ElementDetailsFragment();

        final Bundle args = new Bundle();
        args.putString(ARG_ATOMIC_SYMBOL, atomicSymbol);
        fragment.setArguments(args);

        return fragment;
    }


    public static void showDialog(@NonNull FragmentManager fm, int atomicNumber) {
        getInstance(atomicNumber).show(fm, TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);

        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);

        final Bundle args = getArguments();
        if(args != null) {
            if(args.containsKey(ARG_ATOMIC_NUMBER)) {
                mElement = Elements.getElement(args.getInt(ARG_ATOMIC_NUMBER));
            } else if(args.containsKey(ARG_ATOMIC_SYMBOL)) {
                mElement = Elements.getElement(args.getString(ARG_ATOMIC_SYMBOL));
            }
        }

        mStringUnknown = getString(R.string.unknown);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_element_details, container, false);

        mTxtHeader = root.findViewById(R.id.header);

        mElementBlock = root.findViewById(R.id.elementBlock);
        mTxtElementSymbol = root.findViewById(R.id.elementSymbol);
        mTxtElementNumber = root.findViewById(R.id.elementNumber);
        mTxtElementWeight = root.findViewById(R.id.elementWeight);
        mTxtElementElectrons = root.findViewById(R.id.elementElectrons);

        mTxtNumber = root.findViewById(R.id.number);
        mTxtSymbol = root.findViewById(R.id.symbol);
        mTxtName = root.findViewById(R.id.name);
        mTxtWeight = root.findViewById(R.id.weight);
        mTxtConfiguration = root.findViewById(R.id.config);
        mTxtElectrons = root.findViewById(R.id.electrons);
        mTxtCategory = root.findViewById(R.id.category);
        mTxtGPB = root.findViewById(R.id.gpb);
        mTxtDensity = root.findViewById(R.id.density);
        mTxtMelt = root.findViewById(R.id.melt);
        mTxtBoil = root.findViewById(R.id.boil);
        mTxtHeat = root.findViewById(R.id.heat);
        mTxtNegativity = root.findViewById(R.id.negativity);
        mTxtAbundance = root.findViewById(R.id.abundance);

        mIsoTable = root.findViewById(R.id.isoTable);

        root.findViewById(R.id.videoButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showVideo();
            }
        });

        root.findViewById(R.id.wikiButton).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showWikipedia();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void populateViews() {
        if(mElement == null) {
            return;
        }
        final String name = getString(ElementUtils.getElementName(mElement.number));

        if(!getShowsDialog()) {
            final Activity activity = getActivity();
            if(activity != null) {
                activity.setTitle(getString(R.string.titleElementDetails, name));
            }
        }

        mTxtHeader.setText(name);
        mTxtName.setText(name);

        setBlockBackground();

        getNumber();
        getSymbol();
        getWeight();
        getCategory();
        getGPB();
        getElectronConfiguration();
        getElectrons();
        mTxtDensity.setText(getDensity());
        mTxtMelt.setText(getTemperature(mElement.melt));
        mTxtBoil.setText(getTemperature(mElement.boil));
        mTxtHeat.setText(getHeat());
        mTxtNegativity.setText(getNegativity());
        mTxtAbundance.setText(getAbundance());

        populateIsotopes();
    }

    private void setBlockBackground() {
        final int background = ElementUtils.getElementColor(mElement);
        mElementBlock.setBackgroundColor(background);
    }


    private void getNumber() {
        mTxtNumber.setText(String.valueOf(mElement.number));
        mTxtElementNumber.setText(String.valueOf(mElement.number));
    }


    private void getSymbol() {
        mTxtSymbol.setText(mElement.symbol);
        mTxtSymbol.setContentDescription(mElement.symbol.toUpperCase());
        mTxtElementSymbol.setText(mElement.symbol);
    }


    private void getWeight() {
        if(mElement.unstable) {
            mTxtWeight.setText(String.format(Locale.getDefault(), "[%.0f]", mElement.weight));
            mTxtWeight.setContentDescription(String.valueOf((int)mElement.weight));
        } else {
            mTxtWeight.setText(DECIMAL_FORMAT.format(mElement.weight));
        }
        mTxtElementWeight.setText(mTxtWeight.getText());
    }


    private void getCategory() {
        final CharSequence[] cats = getResources().getTextArray(R.array.ptCategories);
        mTxtCategory.setText(cats[mElement.category]);
    }

    private void getGPB() {
        final StringBuilder builder = new StringBuilder();
        final StringBuilder descBuilder = new StringBuilder();

        if(mElement.group == 0) {
            builder.append("∅, ");
        } else {
            builder.append(mElement.group).append(", ");
            descBuilder.append(getString(R.string.descGroup)).append(' ').append(mElement.group)
                    .append(", ");
        }

        builder.append(mElement.period).append(", ");
        descBuilder.append(getString(R.string.descPeriod)).append(' ').append(mElement.period)
                .append(", ");

        builder.append(mElement.block);
        descBuilder.append(getString(R.string.descBlock)).append(' ')
                .append(String.valueOf(mElement.block).toUpperCase());

        mTxtGPB.setText(builder.toString());
        mTxtGPB.setContentDescription(descBuilder.toString());
    }


    private void getElectronConfiguration() {
        final StringBuilder builder = new StringBuilder();
        final StringBuilder descBuilder = new StringBuilder();

        if(mElement.configuration.baseElement != null) {
            builder.append('[').append(mElement.configuration.baseElement).append("] ");
            final Element baseElement = Elements.getElement(mElement.configuration.baseElement);
            if(baseElement != null) {
                descBuilder.append(getString(ElementUtils.getElementName(baseElement.number)));
                descBuilder.append(", ");
            }
        }

        for(Element.Orbital orbital : mElement.configuration.orbitals) {
            builder.append(orbital.shell).append(orbital.orbital);
            builder.append("<sup><small>").append(orbital.electrons).append("</small></sup> ");
            descBuilder.append(orbital.shell).append(' ');
            descBuilder.append(String.valueOf(orbital.orbital).toUpperCase()).append(' ');
            descBuilder.append(orbital.electrons).append(", ");
        }

        descBuilder.delete(descBuilder.length() - 2, descBuilder.length() - 1);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTxtConfiguration.setText(Html.fromHtml(builder.toString().trim(), 0));
            mTxtConfiguration.setContentDescription(Html.fromHtml(descBuilder.toString(), 0));
        } else {
            mTxtConfiguration.setText(Html.fromHtml(builder.toString().trim()));
            mTxtConfiguration.setContentDescription(Html.fromHtml(descBuilder.toString()));
        }
    }


    private void getElectrons() {
        mTxtElectrons.setText(TextUtils.join(", ", mElement.electrons));
        mTxtElementElectrons.setText(TextUtils.join("\n", mElement.electrons));
    }

    @NonNull
    private String getDensity() {
        if(mElement.density != null) {
            return DECIMAL_FORMAT.format(mElement.density) + " g/cm³";
        }
        return mStringUnknown;
    }

    @NonNull
    private String getTemperature(@Nullable Double kelvin) {
        if(kelvin != null) {
            switch(PreferenceUtils.getPrefTempUnit()) {
                case PreferenceUtils.TEMP_C:
                    return String.format(Locale.getDefault(), "%.2f ℃", UnitUtils.KtoC(kelvin));
                case PreferenceUtils.TEMP_F:
                    return String.format(Locale.getDefault(), "%.2f ℉", UnitUtils.KtoF(kelvin));
                default:
                    return String.format(Locale.getDefault(), "%.2f K", kelvin);
            }
        }

        return mStringUnknown;
    }

    @NonNull
    private String getHeat() {
        if(mElement.heat != null) {
            return DECIMAL_FORMAT.format(mElement.heat) + " J/g·K";
        }
        return mStringUnknown;
    }

    @NonNull
    private String getNegativity() {
        if(mElement.negativity != null) {
            return DECIMAL_FORMAT.format(mElement.negativity);
        }
        return mStringUnknown;
    }

    @NonNull
    private String getAbundance() {
        if(mElement.abundance != null) {
            if(mElement.abundance < 0.001) {
                return "<0.001 mg/kg";
            }
            return DECIMAL_FORMAT.format(mElement.abundance) + " mg/kg";
        }
        return mStringUnknown;
    }


    private void populateIsotopes() {
        final Isotope[] isotopes = Isotopes.getIsotopes(mElement.number);
        if(isotopes != null) {
            final LayoutInflater inflater = getLayoutInflater();
            for(Isotope isotope : isotopes) {
                final TableRow tableRow =
                        (TableRow)inflater.inflate(R.layout.isotope_table_row, mIsoTable, false);

                final TextView symbolText = tableRow.findViewById(R.id.isoSymbol);
                symbolText.setText(isotope.getSymbol());

                final TextView massText = tableRow.findViewById(R.id.isoMass);
                massText.setText(DECIMAL_FORMAT.format(isotope.mass));

                mIsoTable.addView(tableRow);
            }
        }
    }

    private void showVideo() {
        if(mElement == null) {
            return;
        }

        final int resId = ElementUtils.getElementVideo(mElement.number);
        if(resId == 0) {
            return;
        }

        final Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + getString(resId)));
        startActivity(intent);
    }


    private void showWikipedia() {
        if(mElement == null) {
            return;
        }
        final int pageId = ElementUtils.getElementWiki(mElement.number);
        final Uri uri = Uri.parse(String.format("https://%s.m.wikipedia.org/wiki/%s",
                getString(R.string.wikiLang), getString(pageId)));
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(PreferenceUtils.KEY_TEMP_UNITS.equals(key)) {
            if(mElement != null) {
                mTxtMelt.setText(getTemperature(mElement.melt));
                mTxtBoil.setText(getTemperature(mElement.boil));
            }
        } else if(PreferenceUtils.KEY_ELEMENT_COLORS.equals(key)) {
            setBlockBackground();
        }
    }
}
