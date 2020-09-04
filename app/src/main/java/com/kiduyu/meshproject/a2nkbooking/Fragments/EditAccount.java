package com.kiduyu.meshproject.a2nkbooking.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.dd.processbutton.iml.ActionProcessButton;
import com.kiduyu.meshproject.a2nkbooking.R;
import com.kiduyu.meshproject.a2nkbooking.Model.User;




/**
 * Fragment which is used to edit Account details of the user
 */
public class EditAccount extends Fragment {


    public EditAccount() {
    }

    public static EditAccount newInstance() {
        return new EditAccount();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);

        return view;
    }

}
