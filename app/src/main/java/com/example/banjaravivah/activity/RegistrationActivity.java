package com.example.banjaravivah.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.banjaravivah.R;

import java.time.LocalDate;
import java.time.Period;

public class RegistrationActivity extends AppCompatActivity {
    Spinner spinner;
    DatePicker datePicker;
    NumberPicker numberPicker;
    EditText height;
    int flag = 1;
    int start = 1;
    TextView calculated_age;

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
}