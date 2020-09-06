package com.kiduyu.meshproject.a2nkbooking.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kiduyu.meshproject.a2nkbooking.Activities.Home;
import com.kiduyu.meshproject.a2nkbooking.Activities.Login;
import com.kiduyu.meshproject.a2nkbooking.Activities.MapsActivity;
import com.kiduyu.meshproject.a2nkbooking.R;
import com.kiduyu.meshproject.a2nkbooking.Model.User;
import com.kiduyu.meshproject.a2nkbooking.Session.Prevalent;


/**
 * Fragment which displays details about the User
 */
public class MyAccount extends Fragment implements OnMapReadyCallback {

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
    private GoogleMap mMap;
    ImageView qrcode;
    TextView noqr;

    public MyAccount() {
    }

    private static final int REQUEST_LOCATION = 1;

    public static MyAccount newInstance() {
        return new MyAccount();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    LocationManager locationManager;
    String latitude, longitude;
    TextView name, balance, email, mobile, address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);


        name = view.findViewById(R.id.myaccount_name);
        balance = view.findViewById(R.id.myaccount_balance);
        email = view.findViewById(R.id.myaccount_email);
        mobile = view.findViewById(R.id.myaccount_mobile);
        address = view.findViewById(R.id.myaccount_address);


        qrcode = view.findViewById(R.id.qr_code_myacc);
        noqr = view.findViewById(R.id.qr_code_none);

        name.setText("Full Name: " + Prevalent.currentOnlineUser.getName());
        balance.setText("Balance Available: Ksh. " + String.valueOf(Prevalent.currentOnlineUser.getBalance()));
        email.setText("My Email:" + Prevalent.currentOnlineUser.getEmail());
        mobile.setText("My Number: +" + Prevalent.currentOnlineUser.getMobile());

        CheckQrCode(Prevalent.currentOnlineUser.getUserId());
        Grps();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_myaccount);
        mapFragment.getMapAsync(this);

        Button map = view.findViewById(R.id.tomap);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void Grps() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        Context context;
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                address.setText("Physical Address: " + Prevalent.currentOnlineUser.getAddress() + "\n \nYour Current Location Is \nLatitude: " + latitude + "\nLongitude: " + longitude);

                // showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(getActivity(), "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogDark);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void CheckQrCode(String userId) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Bookings").child(userId).exists()) {


                } else {
                    qrcode.setVisibility(View.GONE);
                    noqr.setVisibility(View.VISIBLE);
                    noqr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BookingDialog();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void BookingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogDark);
        builder.setTitle("No Bookings Found")
                .setMessage("Would you like to Book a New Ticket?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //create a link to booking fragment
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
