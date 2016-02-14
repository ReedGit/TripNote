package com.reed.tripnote.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.reed.tripnote.R;
import com.reed.tripnote.tools.FormatTool;

import java.text.Format;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginBtn;
    private TextView forgetTV;
    private TextView registerTV;
    private EditText emailET;
    private EditText passwordET;
    private TextInputLayout emailTIL;
    private TextInputLayout passwordTIL;
    private Toolbar loginToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        loginBtn = (Button) findViewById(R.id.btn_login);
        forgetTV = (TextView) findViewById(R.id.tv_login_forget);
        registerTV = (TextView) findViewById(R.id.tv_login_register);
        emailET = (EditText) findViewById(R.id.et_login_email);
        passwordET = (EditText) findViewById(R.id.et_login_password);
        emailTIL = (TextInputLayout) findViewById(R.id.til_login_email);
        passwordTIL = (TextInputLayout) findViewById(R.id.til_login_password);
        loginToolbar = (Toolbar) findViewById(R.id.toolbar_login);

        loginToolbar.setTitle(R.string.login);
        loginToolbar.setTitleTextColor(Color.WHITE);
        loginToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(loginToolbar);
        loginToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loginBtn.setOnClickListener(this);
        forgetTV.setOnClickListener(this);
        registerTV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login_forget:
                break;
            case R.id.tv_login_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                if (email.isEmpty()){
                    emailET.setError("邮箱不能为空");
                    return;
                }
                if (password.isEmpty()){
                    passwordET.setError("密码不能为空");
                    return;
                }
                if (!FormatTool.isEmail(email)){
                    emailET.setError("邮箱格式不正确");
                    return;
                }
                Toast.makeText(LoginActivity.this,"没有问题",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
