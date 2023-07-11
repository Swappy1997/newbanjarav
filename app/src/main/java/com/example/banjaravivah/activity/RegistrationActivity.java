package com.example.banjaravivah.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
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
    Spinner spinner, stateSpinner, citySpinner, educationSpinner, streamSpinner, anualIncomeSpinner, jobTypeSpinner;
    DatePicker datePicker;
    NumberPicker numberPicker;
    EditText height,occupation;
    int flag = 1;
    int start = 1;
    TextView calculated_age;
    //Hii


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        datePicker = findViewById(R.id.datepicker);
        height = findViewById(R.id.heightbtn);
        calculated_age = findViewById(R.id.age);
        stateSpinner = findViewById(R.id.statespinner);
        citySpinner = findViewById(R.id.cityspinner);
        educationSpinner = findViewById(R.id.eduactionspinner);
        streamSpinner = findViewById(R.id.streamspinner);
        anualIncomeSpinner = findViewById(R.id.anualspinner);
        jobTypeSpinner = findViewById(R.id.employmentspinner);
        occupation = findViewById(R.id.occupationet);
        height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        // numberPicker=findViewById(R.id.num1);
        //method calling
        setProfileCreatedFor();
        setStateSpinner();
        setEducationSpinner();
        setAnualIncomeSpinner();
        setJobTypeSpinner();
occupation.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        setOccupation();
    }
});
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

    }

    public void setOccupation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(RegistrationActivity.this).inflate(R.layout.job_search, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ListView listView = alertDialog.findViewById(R.id.jobsearchlist);
        SearchView searchView=alertDialog.findViewById(R.id.jobsearch);

        ArrayList<String> city_name_array = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://banjaravivah.online/mydemo.php/job", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(MainActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onResponse: " + response);
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject responseObj = j.getJSONObject(i);
                        String cityName = responseObj.getString("job_name");
                        city_name_array.add(new String(cityName));
                    }
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, city_name_array);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    listView.setAdapter(cityAdapter);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        // Override onQueryTextSubmit method which is call when submit query is searched
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            // If the list contains the search query than filter the adapter
                            // using the filter method with the query as its argument
                            if (city_name_array.contains(query)) {
                                cityAdapter.getFilter().filter(query);


                            } else {
                                // Search query not found in List View
                                Toast.makeText(RegistrationActivity.this, "Not found", Toast.LENGTH_LONG).show();
                            }
                            return false;
                        }

                        // This method is overridden to filter the adapter according
                        // to a search query when the user is typing search
                        @Override
                        public boolean onQueryTextChange(String newText) {
                            cityAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                            occupation.setText(view.get(position));
                            alertDialog.dismiss();
                        }
                    });

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

    public void setStateSpinner() {
        ArrayList<String> stateNameArrayList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://banjaravivah.online/mydemo.php/allStates", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//               progressBar.setVisibility(View.GONE);
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
                stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            JSONObject object = response.getJSONObject(i);
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
                ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, stateNameArrayList);
                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stateSpinner.setAdapter(stateAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(RegistrationActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);

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

    private void getCityName(String state_id) {

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
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, city_name_array);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(cityAdapter);

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

    public void setEducationSpinner() {
        ArrayList<String> education_array = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(RegistrationActivity.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://banjaravivah.online/mydemo.php/education", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//               progressBar.setVisibility(View.GONE);
                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        String StateName = responseObj.getString("education");
                        Log.d("TAG", StateName);
                        education_array.add(new String(StateName));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                educationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        try {
                            JSONObject object = response.getJSONObject(i);
                            String id = object.getString("id");
                            setStreamSpinner(id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, education_array);
                stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                educationSpinner.setAdapter(stateAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(RegistrationActivity.this, error.toString(), Toasty.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

    public void setStreamSpinner(String education_code) {
        ArrayList<String> stream_array = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://banjaravivah.online/mydemo.php/stream", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject responseObj = j.getJSONObject(i);
                        String cityName = responseObj.getString("stream_name");
                        stream_array.add(new String(cityName));
                    }
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, stream_array);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    streamSpinner.setAdapter(cityAdapter);

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
                hm.put("education_code", education_code);

                return hm;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void setAnualIncomeSpinner() {
        ArrayList<String> anualIncome_array = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://banjaravivah.online/mydemo.php/anual_income", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject responseObj = j.getJSONObject(i);
                        String cityName = responseObj.getString("anual_income");
                        anualIncome_array.add(new String(cityName));
                    }
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, anualIncome_array);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    anualIncomeSpinner.setAdapter(cityAdapter);

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

    public void setJobTypeSpinner() {
        ArrayList<String> em_type_array = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://banjaravivah.online/mydemo.php/em_type", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray j = new JSONArray(response);
                    for (int i = 0; i < j.length(); i++) {
                        JSONObject responseObj = j.getJSONObject(i);
                        String cityName = responseObj.getString("employement_type");
                        em_type_array.add(new String(cityName));
                    }
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(RegistrationActivity.this, android.R.layout.simple_spinner_item, em_type_array);
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    jobTypeSpinner.setAdapter(cityAdapter);

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
}