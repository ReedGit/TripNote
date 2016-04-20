package com.reed.tripnote.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.reed.tripnote.App;
import com.reed.tripnote.R;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.controller.UserManager;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.LogTool;
import com.reed.tripnote.tools.MD5Tool;
import com.reed.tripnote.tools.RetrofitTool;
import com.reed.tripnote.tools.ToastTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyPassword extends AppCompatActivity {

    private static final String TAG = ModifyPassword.class.toString();

    @Bind(R.id.toolbar)
    public Toolbar passwordToolbar;

    @Bind(R.id.tv_password_email)
    public TextView emailTV;

    @Bind(R.id.et_password_old)
    public EditText oldET;

    @Bind(R.id.et_password_new)
    public EditText newET;

    @Bind(R.id.et_password_confirm)
    public EditText confirmET;

    private UserBean user;

    private ProgressDialog mDlg;
    private Call<JSONObject> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);
        user = ((App) getApplication()).getUser();
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify_information:
                String oldPassword = oldET.getText().toString().trim();
                final String newPassword = newET.getText().toString().trim();
                String rePassword = confirmET.getText().toString().trim();
                if (TextUtils.isEmpty(oldPassword)) {
                    oldET.setError("请输入旧密码");
                    return false;
                }
                if (TextUtils.isEmpty(newPassword)) {
                    newET.setError("请输入新密码");
                    return false;
                }
                if (TextUtils.isEmpty(rePassword)) {
                    confirmET.setError("请再次输入新密码");
                    return false;
                }
                if (newPassword.length() < 6) {
                    newET.setError("新密码不能小于6位");
                    return false;
                }
                if (!newPassword.equals(rePassword)) {
                    confirmET.setError("两次输入新密码不同");
                    return false;
                }
                Map<String, Object> map = new HashMap<>();
                map.put(ConstantTool.NEW_PASSWORD, MD5Tool.compute(newPassword));
                map.put(ConstantTool.PASSWORD, MD5Tool.compute(oldPassword));
                map.put(ConstantTool.TOKEN, user.getToken());
                call = RetrofitTool.getService().setProfile(user.getUserId(), map);
                mDlg = ProgressDialog.show(ModifyPassword.this, null, "修改中......", true);
                call.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        mDlg.cancel();
                        if (response.code() != 200) {
                            ToastTool.show(ModifyPassword.this, response.message());
                            LogTool.e(TAG, "请求出错：" + response.message());
                            return;
                        }
                        LogTool.i(TAG, response.body().toString());
                        JSONObject result = response.body();
                        try {
                            if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                                ToastTool.show(ModifyPassword.this, result.getString(ConstantTool.MSG));
                                return;
                            }
                            user.setPassword(MD5Tool.compute(newPassword));
                            UserManager.loginUser(ModifyPassword.this, user);
                            ToastTool.show(ModifyPassword.this, "修改成功");
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        mDlg.cancel();
                        LogTool.e(TAG, t.getMessage());
                        if (!call.isCanceled()) {
                            ToastTool.show(ModifyPassword.this, t.getMessage());
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        passwordToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(passwordToolbar);
        emailTV.setText(user.getEmail());
        passwordToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
