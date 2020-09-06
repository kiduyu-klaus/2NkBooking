package com.kiduyu.meshproject.a2nkbooking.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.meshproject.a2nkbooking.Activities.Home;
import com.kiduyu.meshproject.a2nkbooking.R;
import com.kiduyu.meshproject.a2nkbooking.Model.User;
import com.kiduyu.meshproject.a2nkbooking.Session.Prevalent;


/**
 * Fragment which is used to edit Account details of the user
 */
public class EditAccount extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        RequestPin(Prevalent.currentOnlineUser.getUserId());
    }

    private void RequestPin(String userId) {
        LayoutInflater li = LayoutInflater.from(getContext());
        final View myview = li.inflate(R.layout.pinrequest, null);

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Wallet").child(userId).exists()) {


                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogDark);
                    builder.setView(myview)
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //create a link to booking fragment

                                    EditText pin1 = myview.findViewById(R.id.pincode);
                                    EditText pin12 = myview.findViewById(R.id.pin_confirm);
                                    String mpin1 = pin1.getText().toString().trim();
                                    String mpin2 = pin12.getText().toString().trim();

                                    if (TextUtils.isEmpty(mpin1)) {
                                        pin1.setError("Required..");
                                        return;
                                    } else if (TextUtils.isEmpty(mpin2)) {
                                        pin12.setError("Required..");
                                        return;
                                    } else if (!mpin1.equals(mpin2)) {
                                        pin12.setError("Not Matched..");
                                    } else
                                    {
                                      // CreateWallet(userId,Prevalent.currentOnlineUser.getName(),Prevalent.currentOnlineUser.getBalance(),mpin1);
                                    }

                                    }
                                }).

                                setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick (DialogInterface dialog,int which){
                                        startActivity(new Intent(getActivity(), Home.class));

                                    }
                                });
                                final AlertDialog alertDialog = builder.create();
                    alertDialog.show();


                            }
                }

                @Override
                public void onCancelled (@NonNull DatabaseError databaseError){

                }
            });
        }

    private void CreateWallet(String userId, String name, double balance, String mpin1) {
        Toast.makeText(getActivity(), "test create wallet", Toast.LENGTH_SHORT).show();
    }
}
