package com.example.banjaravivah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.banjaravivah.activity.VerifyOtp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    EditText phone;
    AppCompatButton sendOtp;
    ImageView googleBtn;
    ProgressBar progressBar;
    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;

    String userid, profie_pic, gender;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //demo
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        phone = findViewById(R.id.phone);
        progressBar = findViewById(R.id.progress);
        sendOtp = findViewById(R.id.sendOtp);
        googleBtn = findViewById(R.id.google);
        mAuth = FirebaseAuth.getInstance();


        // variable for FirebaseAuth class

        // Initialize sign in options the client-id is copied form google-services.json file
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("355946790912-osc6kfnrbu6meknn5bckl5ekmn78gt97.apps.googleusercontent.com")
//                .requestEmail()
//                .build();
//
//        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, googleSignInOptions);
//        googleBtn.setOnClickListener((View.OnClickListener) view -> {
//            // Initialize sign in intent
//            Intent intent = googleSignInClient.getSignInIntent();
//            // Start activity for result
//            startActivityForResult(intent, 100);
//        });
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        if (firebaseUser != null) {
//            // When user already sign in redirect to profile activity
//            startActivity(new Intent(MainActivity.this, DashboardMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//            finish();
//        }
        setSendOtp();
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Check condition
//        if (requestCode == 100) {
//            // When request code is equal to 100 initialize task
//            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
//            // check condition
//            if (signInAccountTask.isSuccessful()) {
//                // When google sign in successful initialize string
//                String s = "Google sign in successful";
//                // Display Toast
//                displayToast(s);
//                // Initialize sign in account
//                try {
//                    // Initialize sign in account
//                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
//                    // Check condition
//                    if (googleSignInAccount != null) {
//                        // When sign in account is not equal to null initialize auth credential
//                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
//                        // Check credential
//                        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                // Check condition
//                                if (task.isSuccessful()) {
//                                    // When task is successful redirect to profile activity display Toast
//
//                                    Intent intent = new Intent(MainActivity.this, DashboardMainActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                    displayToast("Firebase authentication successful");
//                                } else {
//                                    // When task is unsuccessful display Toast
//                                    displayToast("Authentication Failed :" + task.getException().getMessage());
//                                }
//                            }
//                        });
//                    }
//                } catch (ApiException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void setSendOtp() {
        sendOtp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View view) {
                if (!(phone.getText().toString().isEmpty())) {
                    if (phone.getText().toString().length() == 10) {
                        progressBar.setVisibility(View.VISIBLE);
                        sendOtp.setVisibility(View.GONE);
                        String mobile = phone.getText().toString();
                        loginCheck(mobile);

                    } else {
                        Toasty.info(getApplicationContext(), "Enter 10 digit phone number", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toasty.info(getApplicationContext(), "Please enter Phone number", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void loginCheck(String phone) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://banjaravivah.online/mydemo.php/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG", "onResponse: " + response);
                if (response.startsWith("Phone Number not registerd Please Regiester")) {
                    Toasty.info(getApplicationContext(), "Please Register ", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    sendOtp.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    sendOtp.setVisibility(View.VISIBLE);
                    sendVerificationCode(phone);
                    try {
                        JSONArray j = new JSONArray(response);
                        for (int i = 0; i < j.length(); i++) {
                            JSONObject responseObj = j.getJSONObject(i);
                            userid = responseObj.getString("user_id");
                            gender = responseObj.getString("gender");
                            profie_pic = responseObj.getString("profie_pic");

                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.info(getApplicationContext(), "Please Register ", Toast.LENGTH_SHORT).show();

            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("key_phone", phone);
                return hm;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber("+91" + number)            // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // Activity (for callback binding)
                .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            Intent intent = new Intent(MainActivity.this, VerifyOtp.class);
            intent.putExtra("mobile", phone.getText().toString());
            intent.putExtra("otp", s);
            intent.putExtra("userid", userid);
            intent.putExtra("gender", gender);
            intent.putExtra("profilepic", profie_pic);
            startActivity(intent);
            finish();
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();

            progressBar.setVisibility(View.GONE);
            sendOtp.setVisibility(View.VISIBLE);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            sendOtp.setVisibility(View.VISIBLE);
            Toasty.error(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}