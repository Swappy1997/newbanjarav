package com.example.banjaravivah.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.banjaravivah.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class VerifyOtp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String mobile, verificationId, code;
    PinView pinView;
    Button verifyBtn;
    ProgressBar progressBar;
    TextView timer;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        mAuth = FirebaseAuth.getInstance();
        verifyBtn = findViewById(R.id.verify);
        mobile = getIntent().getStringExtra("mobile");
        verificationId = getIntent().getStringExtra("otp");
        pinView = findViewById(R.id.pinview);
        progressBar = findViewById(R.id.progressbar);
        timer = findViewById(R.id.timer);
        setSharedPref();
//        code = pinView.getEditableText().toString();
        new CountDownTimer(30000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                timer.setText("OTP is valid for" + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timer.setText("Resend");
            }
        }.start();

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                verifyBtn.setVisibility(View.GONE);

                verifyCode();
            }
        });
    }

    private void verifyCode() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, pinView.getEditableText().toString());
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            progressBar.setVisibility(View.VISIBLE);
                            verifyBtn.setVisibility(View.GONE);

                            Intent i = new Intent(VerifyOtp.this, DashboardMainActivity.class);
                            i.putExtra("phone", mobile);
                            i.putExtra("gender", "Male");
                            startActivity(i);
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            verifyBtn.setVisibility(View.VISIBLE);
                            Toasty.error(VerifyOtp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void setSharedPref() {

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Registered", true);
        editor.putString("phone", mobile);
        editor.putString("gender", "Male");

        editor.apply();
    }
}