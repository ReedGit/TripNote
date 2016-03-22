package com.reed.tripnote.activities;

import android.app.ProgressDialog;
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
 * 注册页面
 * Created by 伟 on 2016/2/14.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.toString();

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

    private ProgressDialog mDlg;

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
                final String password = passwordEt.getText().toString().trim();
                String rePassword = rePasswordEt.getText().toString().trim();
                if (email.isEmpty()) {
                    emailEt.setError("邮箱不能为空");
                    return;
                }
                if (password.isEmpty()) {
                    passwordEt.setError("密码不能为空");
                    return;
                }
                if (!FormatTool.isEmail(email)) {
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
                Call<JSONObject> call = RetrofitTool.getService().register(email, MD5Tool.compute(password));
                mDlg = ProgressDialog.show(RegisterActivity.this, null, "加载中", true);
                call.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        mDlg.cancel();
                        LogTool.i(TAG, response.body().toString());
                        JSONObject result = response.body();
                        try {
                            if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                                ToastTool.show(RegisterActivity.this, result.getString(ConstantTool.MSG));
                                return;
                            }
                            UserBean user = FormatTool.gson.fromJson(String.valueOf(result.getJSONObject(ConstantTool.USER)), UserBean.class);
                            user.setPassword(MD5Tool.compute(password));
                            UserManager.loginUser(RegisterActivity.this, user);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        mDlg.cancel();
                        ToastTool.show(RegisterActivity.this, "服务器出现问题: " + t.getMessage());
                    }
                });
            }
        });
    }
}
