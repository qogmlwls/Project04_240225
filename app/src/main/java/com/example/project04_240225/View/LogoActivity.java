package com.example.project04_240225.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.project04_240225.Model.UserRepository;
import com.example.project04_240225.R;
import com.example.project04_240225.ViewModel.LoginViewModel;

public class LogoActivity extends AppCompatActivity {

    UserRepository userRepository;

    LoginViewModel loginViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        userRepository = new UserRepository(getApplicationContext());
        loginViewModel = new LoginViewModel(userRepository);

        loginViewModel.CheckLogin();
        loginViewModel.getCheckLoginResult().observe(LogoActivity.this, result -> {
            if(result.isSuccess()){

                Intent intent = new Intent(LogoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
            else{
                Intent intent = new Intent(LogoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}