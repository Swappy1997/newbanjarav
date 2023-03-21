package com.example.banjaravivah.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.banjaravivah.R;
import com.example.banjaravivah.fragment.Dashboard;
import com.google.android.material.navigation.NavigationView;

public class DashboardMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Activity activity;
    ImageView menuIcon;
    static final float END_Scale = 0.7f;
    CoordinatorLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);
        activity = DashboardMainActivity.this;
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        menuIcon = findViewById(R.id.menu_icon);
        frameLayout = findViewById(R.id.linearlayout);
        setNavigationView();
        displayDashBoardFragment();
    }

    private void displayDashBoardFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, new Dashboard()).addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void setNavigationView() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        // navigationView.setCheckedItem(R.id.profile);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.dashboard) {
                    //  loadFragment(new DashboardFragment());
                }
//                else if (id == R.id.profile) {
//                    loadFragment(new EditProfiles());
//                } else if (id == R.id.Like) {
//                    loadFragment(new LikeFragment());
//                }
//                else if (id == R.id.setting) {
//                    loadFragment(new SettingFragment());
//                }
//                else if (id == R.id.logout) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    builder.setMessage(R.string.app_logout_msg)
//                            .setCancelable(false)
//                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
////                                    performLogout();
//                                }
//                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float diffScaledOffset = slideOffset * (1 - END_Scale);
                final float offsetScale = 1 - diffScaledOffset;
                frameLayout.setScaleX(offsetScale);
                frameLayout.setScaleY(offsetScale);

                final float cOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = frameLayout.getWidth() * diffScaledOffset / 2;
                final float xTranslation = cOffset - xOffsetDiff;
                frameLayout.setTranslationX(xTranslation);
            }
        });
    }
}