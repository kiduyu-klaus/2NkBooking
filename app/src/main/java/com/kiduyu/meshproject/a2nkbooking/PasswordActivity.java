package com.kiduyu.meshproject.a2nkbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skydoves.elasticviews.ElasticButton;

public class PasswordActivity extends AppCompatActivity {
    String deviceId;
    EditText login_pass;
    ElasticButton elasticButton;
    ProgressDialog loadingBar;

    TextView textView_message;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        textView_message=findViewById(R.id.tv_3);
        login_pass=findViewById(R.id.tv_2_pass);
        elasticButton=findViewById(R.id.pass_login);
        Context context;
        loadingBar= new ProgressDialog(this);
        elasticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = login_pass.getText().toString();

                if (password.isEmpty())
                {
                    login_pass.setError("Password Is Required..");
                    return;
                }else{
                    loadingBar.setTitle("Verifying");
                    loadingBar.setMessage("Please wait, ...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    AuthorizeLogin(password);
                }
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            deviceId = Settings.Secure.getString(
                    getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }



        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(deviceId).exists()) {
                    final User usersData = dataSnapshot.child("Users").child(deviceId).getValue(User.class);
                    textView_message.setText("Welcome back "+usersData.getName()+", please input your password to continue to Homepage");



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void AuthorizeLogin(final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(deviceId).exists()) {
                    final User usersData = dataSnapshot.child("Users").child(deviceId).getValue(User.class);

                    if (usersData.getPassword().equals(password))
                    {
                        loadingBar.dismiss();
                        Toast.makeText(PasswordActivity.this, "Welcome , you are logged in Successfully...", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(PasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }else {
                        loadingBar.dismiss();
                        Toast.makeText(PasswordActivity.this, "wrong Credentials...", Toast.LENGTH_SHORT).show();

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
