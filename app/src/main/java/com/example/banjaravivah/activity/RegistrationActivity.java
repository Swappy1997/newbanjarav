package com.example.banjaravivah.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {
    Spinner spinner, state_spinner, city_spinner, education_spinner, stream_spinner, employement_type_spinner, anual_income_spinner;
    DatePicker datePicker;
    NumberPicker numberPicker;
    EditText height;
    int flag = 1;
    int start = 1;
    TextView calculated_age;
    EditText occupation_btn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        datePicker = findViewById(R.id.datepicker);
        height = findViewById(R.id.heightbtn);
        calculated_age = findViewById(R.id.age);
        height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        // numberPicker=findViewById(R.id.num1);
        setProfileCreatedFor();
        //setNumberPicker();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                    calculated_age.setText("" + getAge() + "Years");
                    getAge();


                }
            });
        }

//method calling
        setLocation();
        setEducation_spinner();
        setEmployement_type_spinner();
        setAnual_income_spinner();
        setOccupation_btn();

    }

    public void setProfileCreatedFor() {
        spinner = findViewById(R.id.prfileCreatedFor);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Profile_created, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setPrompt("Select Profile created for");
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String value = spinner.getSelectedItem().toString();
                Toast.makeText(RegistrationActivity.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getAge() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        int result = Period.between(
                LocalDate.of(year, month, day),
                LocalDate.now()
        ).getYears();
        return result;
    }

    public void setNumberPicker() {
        NumberPicker numberPicker1 = new NumberPicker(this);
        String[] degreesValues = new String[20];

        for (int i = 0; i < degreesValues.length; i++)
            degreesValues[i] = String.valueOf(i) + (char) 0x00B0;

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(19);
        numberPicker.setDisplayedValues(degreesValues);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(1);
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(RegistrationActivity.this).inflate(R.layout.heightdilog, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final NumberPicker np = (NumberPicker) alertDialog.findViewById(R.id.numberPicker1);
        final NumberPicker np2 = (NumberPicker) alertDialog.findViewById(R.id.numberPicker2);
        Button cancel = (Button) alertDialog.findViewById(R.id.btnTsCancel);
        Button ok = (Button) alertDialog.findViewById(R.id.btnok);
        TextView cm = alertDialog.findViewById(R.id.cm);
        TextView cmtext = alertDialog.findViewById(R.id.text1);
        TextView intext = alertDialog.findViewById(R.id.text2);
        TextView ft = alertDialog.findViewById(R.id.ft);
        cm.setBackground(getResources().getDrawable(R.drawable.circlebg2));

        cm.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                if (flag == 1) {
                    np.setMaxValue(220);
                    np.setMinValue(100);
                    np.setValue(160);
                    np.setWrapSelectorWheel(false);
                    cm.setBackground(getResources().getDrawable(R.drawable.circlebg2));
                    ft.setBackground(getResources().getDrawable(R.drawable.circlebackground));
                    cmtext.setText("cm");
                    intext.setVisibility(View.GONE);
                    np2.setVisibility(View.GONE);
                    flag = 0;
                } else if (flag == 0) {
                    np.setMaxValue(7);
                    np.setMinValue(3);
                    np.setValue(4);
                    np.setWrapSelectorWheel(false);
                    cm.setBackground(getResources().getDrawable(R.drawable.circlebackground));
                    ft.setBackground(getResources().getDrawable(R.drawable.circlebg2));
                    cmtext.setText("ft");
                    intext.setVisibility(View.VISIBLE);
                    np2.setVisibility(View.VISIBLE);
                    flag = 1;
                }


            }
        });
        ft.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    ft.setBackground(getResources().getDrawable(R.drawable.circlebg2));
                    cm.setBackground(getResources().getDrawable(R.drawable.circlebackground));
                    np.setMaxValue(7);
                    np.setMinValue(3);
                    np.setValue(5);
                    np.setWrapSelectorWheel(false);

                    np2.setMaxValue(10);
                    np2.setMinValue(0);
                    np2.setValue(6);
                    np2.setWrapSelectorWheel(false);

                    np2.setVisibility(View.VISIBLE);
                    intext.setVisibility(View.VISIBLE);
                    cmtext.setText("ft");
                    intext.setText("in");
                    flag = 0;
                } else {
                    np.setMaxValue(220);
                    np.setMinValue(100);
                    np.setValue(160);
                    np.setWrapSelectorWheel(false);
                    ft.setBackground(getResources().getDrawable(R.drawable.circlebackground));
                    cm.setBackground(getResources().getDrawable(R.drawable.circlebg2));
                    cmtext.setText("cm");
                    intext.setVisibility(View.GONE);
                    np2.setVisibility(View.GONE);
                    flag = 1;
                }
            }
        });
        np.setMaxValue(220);
        np.setMinValue(100);
        np.setValue(160);
        np.setWrapSelectorWheel(false);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.i("value is", "" + i1);

            }
        });
//        np.setOnValueChangedListener(this);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                height.setText(String.valueOf(np.getValue()));
                alertDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }

    private void setLocation() {
        state_spinner = findViewById(R.id.statespinner);
        ArrayList<String> stateNameArrayList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
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
                ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, stateNameArrayList);
                state_spinner.setAdapter(stateAdapter);
                state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        try {
                            JSONObject object = response.getJSONObject(position);
                            String state_id = object.getString("id");

                            Log.d("TAG", "onItemSelected: " + state_id);
                            getCityName(state_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                Log.d("TAG", "onClick: " + state_spinner.getSelectedItem());
                stateAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(RegistrationActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void getCityName(String state_id) {
        city_spinner = findViewById(R.id.cityspinner);
        Log.d("TAG", "getCityName: " + state_id);
        ArrayList<String> city_name_array = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
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
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, city_name_array);
                    city_spinner.setAdapter(cityAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(RegistrationActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
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

    public void setEducation_spinner() {
        education_spinner = findViewById(R.id.eduactionspinner);
        ArrayList<String> educationArrayList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://banjaravivah.online/mydemo.php/education", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                progressBar.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject responseObj = response.getJSONObject(i);

                        String educationName = responseObj.getString("education");
                        educationArrayList.add(new String(educationName));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ArrayAdapter<String> educationAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, educationArrayList);
                education_spinner.setAdapter(educationAdapter);
                education_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        try {
                            JSONObject object = response.getJSONObject(position);
                            String stream_id = object.getString("id");

                            Log.d("TAG", "onItemSelected: " + stream_id);
                            setStream_spinner(stream_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                educationAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(RegistrationActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void setStream_spinner(String education_id) {
        stream_spinner = findViewById(R.id.streamspinner);
        ArrayList<String> stream_name_array = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://banjaravivah.online/mydemo.php/stream", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject responseObj = j.getJSONObject(i);
                        String stream_name = responseObj.getString("stream_name");
                        stream_name_array.add(new String(stream_name));
                    }
                    ArrayAdapter<String> streamAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_1, stream_name_array);
                    stream_spinner.setAdapter(streamAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(RegistrationActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("education_code", education_id);
                return hm;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void setEmployement_type_spinner() {
        employement_type_spinner = findViewById(R.id.employmentspinner);
        ArrayList<String> employement_type_array = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://banjaravivah.online/mydemo.php/em_type", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject responseObj = j.getJSONObject(i);
                        String stream_name = responseObj.getString("employement_type");
                        employement_type_array.add(new String(stream_name));
                    }
                    ArrayAdapter<String> streamAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_single_choice, employement_type_array);
                    employement_type_spinner.setAdapter(streamAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(RegistrationActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void setAnual_income_spinner() {
        anual_income_spinner = findViewById(R.id.anualspinner);
        ArrayList<String> anual_income_array = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://banjaravivah.online/mydemo.php/anual_income", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject responseObj = j.getJSONObject(i);
                        String stream_name = responseObj.getString("anual_income");
                        anual_income_array.add(new String(stream_name));
                    }
                    anual_income_spinner.setPrompt("Select Annual Income");

                    ArrayAdapter<String> streamAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_list_item_single_choice, anual_income_array);
                    anual_income_spinner.setAdapter(streamAdapter);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(RegistrationActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void setOccupation_btn() {
        occupation_btn = findViewById(R.id.occupationet);
        occupation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> jobArrayList = new ArrayList<>();
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(RegistrationActivity.this).inflate(R.layout.job_search, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                SearchView searchView = alertDialog.findViewById(R.id.jobsearch);
                ListView joblistview = alertDialog.findViewById(R.id.job_listview);
                RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://banjaravivah.online/mydemo.php/job", null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject responseObj = response.getJSONObject(i);

                                String StateName = responseObj.getString("job_name");
                                Log.d("TAG", StateName);
                                jobArrayList.add(new String(StateName));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, jobArrayList);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                        joblistview.setAdapter(stateAdapter);
                        joblistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selectedFromList = (String) (joblistview.getItemAtPosition(i));
                                occupation_btn.setText(" "+selectedFromList);
                                alertDialog.dismiss();
                            }
                        });
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                if (jobArrayList.contains(query)) {
                                    stateAdapter.getFilter().filter(query);
                                } else {
                                    // Search query not found in List View
                                    Toast.makeText(RegistrationActivity.this, "Not found", Toast.LENGTH_LONG).show();
                                }
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                stateAdapter.getFilter().filter(newText);
                                return false;
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toasty.error(RegistrationActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsonArrayRequest);

            }
        });
    }
}

