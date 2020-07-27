package com.kiduyu.meshproject.a2nkbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skydoves.elasticviews.ElasticButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private Uri MImageURI;
    ElasticButton edt_save;
    CircleImageView profile_img;
    EditText Fullname,PhoneNumber,Email,pass,conirm_pass;
    private ProgressDialog loadingBar;
    String deviceId;
    private static final int PICK_IMAGE_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Fullname = findViewById(R.id.edt_profile_fullname);
        mStorageRef= FirebaseStorage.getInstance().getReference().child("Profile");
        PhoneNumber =  findViewById(R.id.edt_profile_phone);
        Email =  findViewById(R.id.edt_profile_email);
        profile_img =  findViewById(R.id.edt_profile_image);
        loadingBar= new ProgressDialog(this);
        pass=findViewById(R.id.edt_profile_password);
        conirm_pass=findViewById(R.id.edt_profile_confirm_pass);
        edt_save=findViewById(R.id.edt_profile_save);

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenFileChooser();
            }
        });

        edt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mfullname=Fullname.getText().toString();
                String memail=Email.getText().toString();
                String mphone=PhoneNumber.getText().toString();
                String mpass=pass.getText().toString();
                String mconfirm_pass=conirm_pass.getText().toString();

                if (TextUtils.isEmpty(mfullname))
                {
                    Fullname.setError("Name Is Required..");
                    return;
                }
                else if (TextUtils.isEmpty(memail))
                {
                    Email.setError("Email Is Required..");
                    return;
                }else if (TextUtils.isEmpty(mpass))
                {
                    pass.setError("Pass Is Required..");
                    return;
                }else if (!mpass.equals(mconfirm_pass))
                {
                    conirm_pass.setError("passwords does not match..");
                    return;
                }
                else if (TextUtils.isEmpty(mphone))
                {
                    PhoneNumber.setError("Phone number is Required");
                } else{
                    loadingBar.setTitle("Creating Profile");
                    loadingBar.setMessage("Please wait, ...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    AllowAccessToAccount(mfullname, memail,mphone,mpass);

                }
            }
        });

    }

    private void AllowAccessToAccount(final String mfullname, final String memail, final String mphone, final String mpass) {

        if (MImageURI!=null){
            final StorageReference filereference= mStorageRef.child(System.currentTimeMillis()
                    +"."+ getFileExtension(MImageURI));
            filereference.putFile(MImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(Uri uri) {
                            final  Uri umageuri = uri;
                            final String imge= umageuri.toString();
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
                            RootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(deviceId);
                            User user= new User(mfullname,memail,mphone,imge,mpass);
                            RootRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(ProfileActivity.this, "Congratulations "+mfullname+", your account has been created.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(ProfileActivity.this, PasswordActivity.class);
                                        startActivity(intent);
                                    }else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                }
            });

        }else{
            Toast.makeText(this,"no image Selected", Toast.LENGTH_LONG).show();
            loadingBar.dismiss();
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cr=this.getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void OpenFileChooser() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,PICK_IMAGE_REQUEST);

        }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK &&
                data!= null && data.getData()!=null){
            MImageURI= data.getData();
            profile_img.setImageURI(MImageURI);

        }

    }
}
