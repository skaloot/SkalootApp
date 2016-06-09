package com.skaloot.skalootapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Homepage extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;

    public static Homepage newInstance(int sectionNumber) {
        Homepage fragment = new Homepage();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.activity_home_1, container, false);
        }
        return rootView;
    }

}
