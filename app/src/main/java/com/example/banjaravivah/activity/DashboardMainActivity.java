package com.example.banjaravivah.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.banjaravivah.MainActivity;
import com.example.banjaravivah.R;
import com.example.banjaravivah.fragment.Dashboard;
import com.example.banjaravivah.helper.Allusers;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class DashboardMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Activity activity;
    ImageView menuIcon;
    static final float END_Scale = 0.7f;
    CoordinatorLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    int SELECT_PICTURE = 200;
    CircleImageView profile_img;
    String image, phone_number, gender, userid, pic;
    ArrayList<Allusers> allusersArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);
        activity = DashboardMainActivity.this;
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        TextView name = headerview.findViewById(R.id.headername);
        name.setText("Hiii Swappy");
        Intent intent = getIntent();
        phone_number = intent.getStringExtra("phone");
        gender = intent.getStringExtra("gender");
        userid = intent.getStringExtra("userid");
        pic = intent.getStringExtra("pic");

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(DashboardMainActivity.this);

        phone_number = sharedPref.getString("phone", "");
        gender = sharedPref.getString("gender", "");
        userid = sharedPref.getString("userid", "");
        pic = sharedPref.getString("pic", "");

        getUserData();

        Log.d("TAG", "onCreate: " + phone_number + gender);
        profile_img = headerview.findViewById(R.id.circleimage);
        //   getImage();
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
        menuIcon = findViewById(R.id.menu_icon);
        frameLayout = findViewById(R.id.linearlayout);
        bottomNavigationView = findViewById(R.id.bottomnav);
        setNavigationView();
        // displayDashBoardFragment();
        setBottomNavigationView();
    }

    private void getImage() {
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardMainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://banjaravivah.online/mydemo.php/upimage", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Toast.makeText(MainActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray j = new JSONArray(response);
                    JSONObject responseObj = j.getJSONObject(1);
                    image = responseObj.getString("profie_pic");
                    Log.d("TAG", "image" + image);

                    Glide.with(DashboardMainActivity.this).load("https://banjaravivah.online/images/" + image)
                            .into(profile_img);
//                    imageView2.setImageURI(Uri.parse(image));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("key_userid", userid);

                return hm;
            }
        });

        requestQueue.add(stringRequest);
    }

    void imageChooser() {

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {

                    // update the preview image in the layout
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        Log.d("TAG", "onActivityResult1: " + bitmap);


                        profile_img.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //  Glide.with(this).load(selectedImageUri).into(profile_img);

                    // profile_img.setImageURI(selectedImageUri);
                    Log.d("TAG", "onActivityResult: " + selectedImageUri);
                }
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void displayDashBoardFragment(ArrayList<Allusers> allusersArrayList) {
        Dashboard dashboard = new Dashboard();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, dashboard).addToBackStack(null);
        Bundle bundle = new Bundle();
        bundle.putSerializable("alluserlist", allusersArrayList);
        bundle.putInt("key", 1);
        dashboard.setArguments(bundle);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (currentFragment instanceof Dashboard) {
            finish();
        } else {
            super.onBackPressed();
        }
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
                    setLocation();
                    //  loadFragment(new DashboardFragment());
                } else if (id == R.id.logout) {
                    logout();
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

    private void logout() {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DashboardMainActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            Intent i = new Intent(DashboardMainActivity.this, MainActivity.class);
            startActivity(i);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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

    private void setBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.dashboard) {

                } else if (id == R.id.location) {
                    setLocation();
                } else if (id == R.id.other) {
                    showBottomSheetDialog();
                }

                return true;
            }
        });
    }

    private void setLocation() {

        ArrayList<String> stateNameArrayList = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardMainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(DashboardMainActivity.this).inflate(R.layout.education_dilog_box, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Spinner mainDegreeSpinner;
        mainDegreeSpinner = alertDialog.findViewById(R.id.allDegree_Spinner);
        Button cancel = alertDialog.findViewById(R.id.btnTsCancel);
        Button search = alertDialog.findViewById(R.id.btnSearch);
        TextView headertextview = alertDialog.findViewById(R.id.txtTargetMsgHeader);
        TextView choose = alertDialog.findViewById(R.id.choose);
        TextView choose1 = alertDialog.findViewById(R.id.choose2);
        headertextview.setText("Filter By Location");
        choose.setText("Select State");
        choose1.setText("Select City");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(DashboardMainActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://banjaravivah.online/mydemo.php/allStates", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                progressBar.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject responseObj = response.getJSONObject(i);

                        String StateName = responseObj.getString("state");
                        Log.d("TAG", StateName);
                        stateNameArrayList.add(new String(StateName));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(DashboardMainActivity.this, android.R.layout.simple_spinner_item, stateNameArrayList);
                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mainDegreeSpinner.setAdapter(stateAdapter);
                mainDegreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        try {
                            JSONObject object = response.getJSONObject(position);
                            String state_id = object.getString("id");

                            Log.d("TAG", "onItemSelected: " + state_id);
                            getCityName(alertDialog, state_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                Log.d("TAG", "onClick: " + mainDegreeSpinner.getSelectedItem());
                stateAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(DashboardMainActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void getCityName(AlertDialog alertDialog, String state_id) {
        Spinner citySpinner;
        citySpinner = alertDialog.findViewById(R.id.all_subDegree_Spinner);
        Log.d("TAG", "getCityName: " + state_id);
        ArrayList<String> city_name_array = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardMainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://banjaravivah.online/mydemo.php/city", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(MainActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onResponse: " + response);
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject responseObj = j.getJSONObject(i);
                        String cityName = responseObj.getString("city_name");
                        Log.d("TAG", "citynamee" + cityName);
                        city_name_array.add(new String(cityName));
                    }
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(DashboardMainActivity.this, android.R.layout.simple_spinner_item, city_name_array);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(DashboardMainActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("state_code", state_id);

                return hm;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);


        LinearLayout copy = bottomSheetDialog.findViewById(R.id.copyLinearLayout);
        LinearLayout share = bottomSheetDialog.findViewById(R.id.shareLinearLayout);
        LinearLayout upload = bottomSheetDialog.findViewById(R.id.uploadLinearLaySout);
        LinearLayout download = bottomSheetDialog.findViewById(R.id.download);
        LinearLayout delete = bottomSheetDialog.findViewById(R.id.delete);

        bottomSheetDialog.show();
    }

    private void getUserData() {
        allusersArrayList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(DashboardMainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://banjaravivah.online/mydemo.php/alluser", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(DashboardMainActivity.this, response, Toast.LENGTH_SHORT).show();
                try {
                    if (!response.isEmpty()) {
                        JSONArray j = new JSONArray(response);

                        for (int i = 0; i < j.length(); i++) {
                            JSONObject responseObj = j.getJSONObject(i);


                            allusersArrayList.add(new Allusers(Integer.parseInt(responseObj.optString("user_id")), responseObj.getString("profile_created")
                                    , responseObj.getString("phone_number"), responseObj.getString("first_name"), responseObj.getString("last_name"), responseObj.getString("profie_pic"),
                                    responseObj.getString("gender"), responseObj.getString("marital_status"), responseObj.getString("education"), responseObj.getString("stream"),
                                    responseObj.getString("state"), responseObj.getString("city"), responseObj.getString("age"), responseObj.getString("employement_type"),
                                    responseObj.getString("anual_income"), responseObj.getString("height"), responseObj.getString("occupation"), responseObj.getString("father_name"),
                                    responseObj.getString("mother_name"), responseObj.getString("village_name"), responseObj.getString("no_sister")));

                            Glide.with(DashboardMainActivity.this).load("https://banjaravivah.online/images/" + pic)
                                    .into(profile_img);
                        }

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                displayDashBoardFragment(allusersArrayList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(DashboardMainActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("key_phone", phone_number);
                hm.put("key_gender", gender);

                return hm;
            }
        };
        requestQueue.add(stringRequest);
    }
}