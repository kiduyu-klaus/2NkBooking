package com.kiduyu.meshproject.a2nkbooking.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dd.processbutton.ProcessButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.meshproject.a2nkbooking.Model.User;
import com.kiduyu.meshproject.a2nkbooking.R;
import com.kiduyu.meshproject.a2nkbooking.Session.Prevalent;

import java.util.Objects;

import io.paperdb.Paper;


public class Login extends AppCompatActivity {

    private EditText phonenumber;
    private EditText pass;
    private CheckBox chkBoxRememberMe;
    private ProcessButton btnLogin;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Login");
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));


        phonenumber = findViewById(R.id.number_login);
        pass = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);

        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        Paper.init(this);
    }


    public void forgotPasssword(View view) {
        SpannableString message = new SpannableString("To reset Password visit \nwww.ibts.com/forgotpassword.php");
        Linkify.addLinks(message, Linkify.ALL);
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this, R.style.AlertDialogLight);
        builder.setCancelable(false);
        builder.setTitle("Password Reset");
        builder.setMessage(message);
        builder.setPositiveButton("Okay", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    public void createAccount(View view) {
        startActivity(new Intent(Login.this, SignUp.class));
    }


    public void LoginToapp(View view) {
        String phone = phonenumber.getText().toString();
        String password = pass.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            phonenumber.setError("Phone Number Is Required..");
            return;
        } else if (TextUtils.isEmpty(password)) {
            pass.setError("Password Is Required..");
            return;
        } else {
            loadingBar.setTitle("Logging Into The App");
            loadingBar.setMessage("Please wait, while we are checking the credentials...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            String uniqueid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            AllowAccessToAccount(uniqueid, phone, password);


        }
    }

    private void AllowAccessToAccount(String uniqueid, String phone, String password) {

        if (chkBoxRememberMe.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(uniqueid).exists()) {
                    User usersData = dataSnapshot.child("Users").child(uniqueid).getValue(User.class);

                    if (usersData.getMobile().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {

                            Toast.makeText(Login.this, "Welcome "+usersData.getName()+", you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(Login.this, Home.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(Login.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Login.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}