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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 登录页面
 * Created by 伟 on 2016/2/14.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.btn_login)
    public Button loginBtn;
    @Bind(R.id.tv_login_forget)
    public TextView forgetTV;
    @Bind(R.id.tv_login_register)
    public TextView registerTV;
    @Bind(R.id.et_login_email)
    public EditText emailET;
    @Bind(R.id.et_login_password)
    public EditText passwordET;
    @Bind(R.id.til_login_email)
    public TextInputLayout emailTIL;
    @Bind(R.id.til_login_password)
    public TextInputLayout passwordTIL;
    @Bind(R.id.toolbar_login)
    public Toolbar loginToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
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
        switch (v.getId()) {
            case R.id.tv_login_forget:
                break;
            case R.id.tv_login_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                if (email.isEmpty()) {
                    emailET.setError("邮箱不能为空");
                    return;
                }
                if (password.isEmpty()) {
                    passwordET.setError("密码不能为空");
                    return;
                }
                if (!FormatTool.isEmail(email)) {
                    emailET.setError("邮箱格式不正确");
                    return;
                }
                Toast.makeText(LoginActivity.this, "没有问题", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
