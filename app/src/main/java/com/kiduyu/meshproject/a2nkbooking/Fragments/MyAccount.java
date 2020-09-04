package com.kiduyu.meshproject.a2nkbooking.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.kiduyu.meshproject.a2nkbooking.R;
import com.kiduyu.meshproject.a2nkbooking.Model.User;





/**
 * Fragment which displays details about the User
 */
public class MyAccount extends Fragment {

    ImageView imageQRCode;
    private int userId;
    private String dataName;
    private String dataMobile;
    private String dataEmail;
    private String dataAddress;
    private String password;
    private String qrCodeData;
    private double dataBalance;
    private String serverMessage;
    private int serverSuccess;

    public MyAccount() {
    }

    public static MyAccount newInstance() {
        return new MyAccount();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);


        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
