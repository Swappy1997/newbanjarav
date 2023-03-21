package com.example.banjaravivah.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.banjaravivah.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Dashboard extends Fragment {
    View view;
    Button btLogout;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

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

        return view;

    }
}