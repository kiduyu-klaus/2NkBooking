package com.kiduyu.meshproject.a2nkbooking.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.kiduyu.meshproject.a2nkbooking.R;


/**
 * Fragment which lets users to change different settings of the Application
 */
public class Settings extends Fragment {

    public Settings() {
    }


    public static Settings newInstance() {
        return new Settings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}
