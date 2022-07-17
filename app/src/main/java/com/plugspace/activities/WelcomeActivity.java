package com.plugspace.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.plugspace.R;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {
    Activity activity;
    Button btnSignUp, btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        activity = WelcomeActivity.this;
        initView();
        initClick();
    }

    private void initClick() {
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    private void initView() {
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            startActivity(new Intent(activity, LoginActivity.class));

        } else if (view.getId() == R.id.btnSignUp) {
            startActivity(new Intent(activity, SignUpActivity.class));
        }
    }
}
