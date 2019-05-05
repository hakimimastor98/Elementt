package com.kimi.elementsss.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kimi.elementsss.R;


public class TableFragment extends Fragment{



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_table, container, false);
        return rootView;


    }


}

