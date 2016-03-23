package com.reed.tripnote.activities;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.reed.tripnote.R;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;
import com.reed.tripnote.tools.LogTool;
import com.reed.tripnote.tools.RetrofitTool;
import com.reed.tripnote.tools.ToastTool;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ForgetActivity.class.toString();

    @Bind(R.id.et_forget_mail)
    public TextInputEditText emailET;
    private ProgressDialog mDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forget:
                String email = emailET.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    ToastTool.show(ForgetActivity.this, R.string.email_hint);
                    return;
                }
                if (!FormatTool.isEmail(email)) {
                    ToastTool.show(ForgetActivity.this, "邮箱格式不正确");
                    return;
                }
                Call<JSONObject> call = RetrofitTool.getService().reset(email);
                mDlg = ProgressDialog.show(ForgetActivity.this, null, "请稍后.......", true);
                call.enqueue(new Callback<JSONObject>() {
                    @Override
                    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                        mDlg.cancel();
                        LogTool.i(TAG, response.body().toString());
                        JSONObject result = response.body();
                        try {
                            if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                                ToastTool.show(ForgetActivity.this, result.getString(ConstantTool.MSG));
                                return;
                            }
                            ToastTool.show(ForgetActivity.this, "邮件已经发送成功");
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<JSONObject> call, Throwable t) {
                        mDlg.cancel();
                        ToastTool.show(ForgetActivity.this, "服务器出现问题: " + t.getMessage());
                    }
                });
                break;
        }
    }
}
