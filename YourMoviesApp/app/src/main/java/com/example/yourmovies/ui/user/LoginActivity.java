package com.example.yourmovies.ui.user;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourmovies.R;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.login_frame, new FragmentLogin(), "LOGIN");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void switchToRegistration(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_frame, new FragmentRegistration(), "REGISTER");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void switchToLogin(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.login_frame, new FragmentLogin(), "LOGIN");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {

    }
}
