package com.kiduyu.meshproject.a2nkbooking.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kiduyu.meshproject.a2nkbooking.R;

/**
 * Fragment which displays travel made by the User
 */
public class TravelHistory extends Fragment {
    public TravelHistory() {
    }

    public static TravelHistory newInstance() {
        return new TravelHistory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_travel_history, container, false);
    }

}
