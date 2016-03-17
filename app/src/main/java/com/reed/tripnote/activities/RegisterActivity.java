package com.reed.tripnote.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.reed.tripnote.R;
import com.reed.tripnote.tools.FormatTool;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 注册页面
 * Created by 伟 on 2016/2/14.
 */
public class RegisterActivity extends AppCompatActivity {

    @Bind(R.id.toolbar_register)
    public Toolbar registerToolBar;

    @Bind(R.id.btn_register)
    public Button registerBtn;

    @Bind(R.id.et_register_email)
    public TextInputEditText emailEt;

    @Bind(R.id.et_register_password)
    public TextInputEditText passwordEt;

    @Bind(R.id.et_register_re_password)
    public TextInputEditText rePasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initView() {
        registerToolBar.setTitle(R.string.register);
        registerToolBar.setTitleTextColor(Color.WHITE);
        registerToolBar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(registerToolBar);
    }

    private void initListener() {
        registerToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String rePassword = rePasswordEt.getText().toString().trim();
                if (email.isEmpty()) {
                    emailEt.setError("邮箱不能为空");
                    return;
                }
                if (password.isEmpty()) {
                    passwordEt.setError("密码不能为空");
                    return;
                }
                if (FormatTool.isEmail(email)) {
                    emailEt.setError("邮箱格式不正确");
                    return;
                }
                if (password.length() < 6) {
                    passwordEt.setError("密码长度不能小于6位");
                    return;
                }
                if (!password.equals(rePassword)) {
                    rePasswordEt.setError("两次输入密码不一致");
                    return;
                }
                Toast.makeText(RegisterActivity.this, "没有问题", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
