package com.reed.tripnote.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.reed.tripnote.R;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.controller.UserManager;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;
import com.reed.tripnote.tools.LogTool;
import com.reed.tripnote.tools.MD5Tool;
import com.reed.tripnote.tools.RetrofitTool;
import com.reed.tripnote.tools.ToastTool;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录页面
 * Created by 伟 on 2016/2/14.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.toString();

    @Bind(R.id.btn_login)
    public Button loginBtn;
    @Bind(R.id.tv_login_forget)
    public TextView forgetTV;
    @Bind(R.id.tv_login_register)
    public TextView registerTV;
    @Bind(R.id.et_login_email)
    public TextInputEditText emailET;
    @Bind(R.id.et_login_password)
    public TextInputEditText passwordET;
    @Bind(R.id.toolbar_login)
    public Toolbar loginToolbar;

    private ProgressDialog mDlg;
    private Call<JSONObject> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
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
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_login_forget:
                intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                String email = emailET.getText().toString().trim();
                final String password = passwordET.getText().toString().trim();
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
                call = RetrofitTool.getService().login(email, MD5Tool.compute(password));
                mDlg = ProgressDialog.show(LoginActivity.this, null, "正在登陆，请稍后......", true);
                call.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        mDlg.cancel();
                        if (response.code() != 200) {
                            ToastTool.show(LoginActivity.this, response.message());
                            LogTool.e(TAG, "请求出错：" + response.message());
                            return;
                        }
                        LogTool.i(TAG, response.body().toString());
                        JSONObject result = response.body();
                        try {
                            if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                                ToastTool.show(LoginActivity.this, result.getString(ConstantTool.MSG));
                                return;
                            }
                            UserBean user = FormatTool.gson.fromJson(String.valueOf(result.getJSONObject(ConstantTool.DATA)), UserBean.class);
                            user.setPassword(MD5Tool.compute(password));
                            LogTool.i(TAG,user.toString());
                            UserManager.loginUser(LoginActivity.this, user);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        mDlg.cancel();
                        ToastTool.show(LoginActivity.this, "服务器出现问题: " + t.getMessage());
                    }
                });
                break;
        }
    }
}
