package com.example.banjaravivah.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.example.banjaravivah.R;
import com.example.banjaravivah.adapter.UserDataAdapter;
import com.example.banjaravivah.helper.Allusers;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class Dashboard extends Fragment {
    View view;
    Button btLogout;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    RecyclerView recyclerView;
    RelativeLayout locationBtn;
    JSONArray stateArray;
    String[] names;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = view.findViewById(R.id.listview);
         progressBar = view.findViewById(R.id.dashboardpb);
        //locationBtn = view.findViewById(R.id.locationLayout);
        // int[] img = {R.drawable.ic_heart, R.drawable.googlelogo, R.drawable.baseline_email_24, R.drawable.ic_baseline_work_outline_24};
        //  ArrayList<String> arrayList=new ArrayList<>(names);

        loadList();

//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            String phone = bundle.getString("phone");
//            String gender = bundle.getString("phone");
//        }


        // Initialize firebase auth
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        // Initialize firebase user
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // Check condition
//        if (firebaseUser != null) {
//            // When firebase user is not equal to null set image on image view
//            Glide.with(ProfileActivity.this).load(firebaseUser.getPhotoUrl()).into(ivImage);
//            // set name on text view
//            tvName.setText(firebaseUser.getDisplayName());
//        }
//        googleSignInClient = GoogleSignIn.getClient(getContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
//
//        btLogout.setOnClickListener(view -> {
//            // Sign out from google
//            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    // Check condition
//                    if (task.isSuccessful()) {
//                        // When task is successful sign out from firebase
//                        firebaseAuth.signOut();
//                        // Display Toast
//                        Toast.makeText(getContext(), "Logout successful", Toast.LENGTH_SHORT).show();
//                        // Finish activity
//                        //finish();
//                    }
//                }
//            });
//        });
        // setLocationBtn();
        return view;

    }

    public void loadList() {
        ArrayList<Allusers> transactionList = (ArrayList<Allusers>) getArguments().getSerializable("alluserlist");
        int key = getArguments().getInt("key");

        if (key == 1) {
            UserDataAdapter userDataAdapter = new UserDataAdapter(getContext(), transactionList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(userDataAdapter);
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    private void setLocationBtn() {
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> stateNameArrayList = new ArrayList<>();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.education_dilog_box, viewGroup, false);
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
                headertextview.setText("Search By Location");
                choose.setText("Select State");
                choose1.setText("Select City");
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                RequestQueue queue = Volley.newRequestQueue(getContext());
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
                        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, stateNameArrayList);
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
                        Toasty.error(getContext(), error.toString(), Toasty.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsonArrayRequest);
            }

        });
    }

    private void getCityName(AlertDialog alertDialog, String state_id) {
        Spinner citySpinner;
        citySpinner = alertDialog.findViewById(R.id.all_subDegree_Spinner);
        Log.d("TAG", "getCityName: " + state_id);
        ArrayList<String> city_name_array = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, city_name_array);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(), error.toString(), Toasty.LENGTH_SHORT).show();
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

}