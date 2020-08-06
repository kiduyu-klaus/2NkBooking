package com.kiduyu.meshproject.a2nkbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.shashank.sony.fancytoastlib.FancyToast;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar=findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);

        drawer= findViewById(R.id.drawer_layout);
        NavigationView navigationView= findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView user= headerView.findViewById(R.id.nav_header_name);
        TextView phone= headerView.findViewById(R.id.nav_header_phone);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem Item) {
                switch (Item.getItemId()) {
                    case R.id.nav_book:
                      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new BookTicketFragment()).commit();



                        break;
                    case R.id.nav_wallet:
                         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                              new MyBookingsFragment()).commit();

                        break;
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                               new HomeFragment()).commit();

                        break;
                    case R.id.nav_mybookings:
                        //   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        //         new Farmer_UpdatesFragment()).commit();

                        break;

                    case R.id.nav_invites:
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        //       new Find_Vet_Fragment()).commit();

                        break;


                    case R.id.nav_share:

                        FancyToast.makeText(HomeActivity.this, "Share this app", FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();
                        break;

                    case R.id.logout:

                        // Toast.makeText(UserHomeActivity.this, "Send this app", Toast.LENGTH_SHORT).show();
                        FancyToast.makeText(HomeActivity.this, "Send this app", FancyToast.LENGTH_SHORT,FancyToast.INFO,false).show();

                        break;

                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState== null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);}
    }
}