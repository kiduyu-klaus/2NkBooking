package com.kiduyu.meshproject.a2nkbooking.Activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.kiduyu.meshproject.a2nkbooking.Fragments.EditAccount;
import com.kiduyu.meshproject.a2nkbooking.Fragments.Help;
import com.kiduyu.meshproject.a2nkbooking.Fragments.MyAccount;
import com.kiduyu.meshproject.a2nkbooking.Fragments.Settings;
import com.kiduyu.meshproject.a2nkbooking.Fragments.TravelHistory;
import com.kiduyu.meshproject.a2nkbooking.R;
import com.kiduyu.meshproject.a2nkbooking.Session.Prevalent;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    FloatingActionButton floatingActionButton;

    DrawerLayout drawerLayout;

    NavigationView navigationView;
    private FragmentManager fragmentManager;
    private boolean backPressed = false;


    public void floatingButtonPressed() {
        Fragment current = fragmentManager.findFragmentById(R.id.activity_root_layout_linear);
        if (current instanceof MyAccount) {
            EditAccount editAccount = (EditAccount) fragmentManager.findFragmentByTag("EDIT_ACCOUNT");

            replaceFragment(editAccount, "EDIT_ACCOUNT", "Edit Account");
            floatingActionButton.setVisibility(View.INVISIBLE);
            navigationView.setCheckedItem(R.id.menu_edit_details);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        toolbar = findViewById(R.id.app_bar);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        floatingActionButton = findViewById(R.id.floating_action_button);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        TextView user= headerView.findViewById(R.id.navigation_header_name);
        TextView phone= headerView.findViewById(R.id.navigation_header_mobile_number);

        user.setText(Prevalent.currentOnlineUser.getName());
        phone.setText("+"+Prevalent.currentOnlineUser.getMobile());



        fragmentManager = getSupportFragmentManager();
        MyAccount myAccount = (MyAccount) getSupportFragmentManager().findFragmentByTag("MY_ACCOUNT");
        if (myAccount == null) {
            myAccount = MyAccount.newInstance();
        }
        replaceFragment(myAccount, "MY_ACCOUNT", "My Account");
        navigationView.setCheckedItem(R.id.menu_my_account);
        //String userName = User.getUserObject(readFromSharedPreferences(Constants.USER_DATA_OBJECT, "")).getName();
        String userName= "Kiduyu Klaus";
        if (userName.contentEquals("-")) {
            getSupportActionBar().setSubtitle(null);
        } else {
            getSupportActionBar().setSubtitle(userName);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backPressed) {
                super.onBackPressed();
                return;
            }
            this.backPressed = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressed = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        navigationView.setCheckedItem(id);
        if (id == R.id.menu_my_account) {
            MyAccount myAccount = (MyAccount) fragmentManager.findFragmentByTag("MY_ACCOUNT");
            if (myAccount == null) {
                myAccount = MyAccount.newInstance();
            }
            replaceFragment(myAccount, "MY_ACCOUNT", "My Account");
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setImageResource(R.drawable.icon_edit);
        } else if (id == R.id.menu_edit_details) {
            EditAccount editAccount = (EditAccount) new EditAccount();

            replaceFragment(editAccount, "EDIT_ACCOUNT", "Edit Account");
            floatingActionButton.setVisibility(View.INVISIBLE);
        } else if (id == R.id.menu_settings) {
            Settings settings = (Settings) fragmentManager.findFragmentByTag("SETTINGS");
            if (settings == null) {
                settings = Settings.newInstance();
            }
            replaceFragment(settings, "SETTINGS", "Settings");
            floatingActionButton.setVisibility(View.INVISIBLE);
        } else if (id == R.id.menu_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogDark);
            AlertDialog dialog;
            builder.setCancelable(false);
            builder.setTitle("Logout of Application?");
            builder.setMessage("Do you want to logout of the Application");
            builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   //add clear login details
                    startActivity(new Intent(Home.this, Login.class));
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Fragment fragment = fragmentManager.findFragmentById(R.id.activity_root_layout_linear);
                    if (fragment instanceof MyAccount) {
                        navigationView.setCheckedItem(R.id.menu_my_account);
                    } else if (fragment instanceof EditAccount) {
                        navigationView.setCheckedItem(R.id.menu_edit_details);
                    }
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

        } else if (id == R.id.travel_history) {

            TravelHistory travelHistory = (TravelHistory) fragmentManager.findFragmentByTag("TRAVEL_HISTORY");
            if (travelHistory == null) {
                travelHistory = TravelHistory.newInstance();
            }
            replaceFragment(travelHistory, "TRAVEL_HISTORY", "Travel History");
            floatingActionButton.setVisibility(View.INVISIBLE);

        } else if (id == R.id.bus_tracker) {


            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setImageResource(R.drawable.icon_share);

        } else if (id == R.id.stop_details) {

            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setImageResource(R.drawable.icon_share);

        } else if (id == R.id.travel_book) {

            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setImageResource(R.drawable.icon_share);

        } else if (id == R.id.help) {
            Help help = (Help) fragmentManager.findFragmentByTag("HELP");
            if (help == null) {
                help = Help.newInstance();
            }
            replaceFragment(help, "HELP", "Help");
            floatingActionButton.setVisibility(View.INVISIBLE);

        } else if (id == R.id.about) {

            floatingActionButton.setVisibility(View.INVISIBLE);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    private void replaceFragment(Fragment fragment, String tag, String actionBarTitle) {
        fragmentManager.beginTransaction().replace(R.id.activity_root_layout_linear, fragment, tag).commit();
        getSupportActionBar().setTitle(actionBarTitle);
    }
}
