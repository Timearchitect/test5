package com.timearchitect.test5;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class BlankFragment extends Fragment {

    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v=  inflater.inflate(R.layout.fragment_blank, container, false);


       FrameLayout fl= v.findViewById(R.id.frame1); //Nice det funkar efter inflate !!!

        return v;
    }
}