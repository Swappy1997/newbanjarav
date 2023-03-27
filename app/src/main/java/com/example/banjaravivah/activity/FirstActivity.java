package com.example.banjaravivah.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.banjaravivah.MainActivity;
import com.example.banjaravivah.R;

import xyz.hanks.library.bang.SmallBangView;

public class FirstActivity extends AppCompatActivity {
    SmallBangView imageView;
    AppCompatButton loginBtn, regBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        loginBtn = findViewById(R.id.login);
        regBtn = findViewById(R.id.reg);
//        imageView = findViewById(R.id.like_heart);
//        imageView.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (imageView.isSelected()) {
//                            imageView.setSelected(false);
//                        } else {
//                            // if not selected only
//                            // then show animation.
//                            imageView.setSelected(true);
//                            imageView.likeAnimation();
//                        }
//                    }
//                });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean Registered;
                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(FirstActivity.this);
                Registered = sharedPref.getBoolean("Registered", false);

                if (!Registered) {
                    startActivity(new Intent(FirstActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(FirstActivity.this, DashboardMainActivity.class));
                }
                finish();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                finish();
            }
        });
    }
}