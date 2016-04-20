package com.reed.tripnote.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.reed.tripnote.App;
import com.reed.tripnote.R;
import com.reed.tripnote.beans.TravelBean;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;
import com.reed.tripnote.tools.LogTool;
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

public class CreateTravelActivity extends AppCompatActivity {

    private static final String TAG = CreateTravelActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    public Toolbar travelToolbar;
    @Bind(R.id.et_travel_title)
    public EditText titleET;
    @Bind(R.id.et_travel_introduction)
    public EditText introductionET;
    private UserBean user;
    private ProgressDialog mDlg;
    private Call<JSONObject> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_travel);
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
        if (mDlg != null && mDlg.isShowing()) {
            mDlg.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_publish:
                String title = titleET.getText().toString().trim();
                String introduction = introductionET.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    titleET.setError("请输入游记名称");
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", title);
                    map.put("introduction", introduction);
                    map.put("userId", user.getUserId());
                    call = RetrofitTool.getService().createTravel(map);
                    mDlg = ProgressDialog.show(CreateTravelActivity.this, null, "请稍后......", true);
                    call.enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            mDlg.cancel();
                            if (response.code() != 200) {
                                ToastTool.show(CreateTravelActivity.this, response.message());
                                LogTool.e(TAG, "请求出错：" + response.message());
                                return;
                            }
                            LogTool.i(TAG, response.body().toString());
                            JSONObject result = response.body();
                            try {
                                if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK){
                                    ToastTool.show(CreateTravelActivity.this, result.getString(ConstantTool.MSG));
                                    return;
                                }
                                TravelBean travel = FormatTool.gson.fromJson(String.valueOf(result.getJSONObject(ConstantTool.DATA)), TravelBean.class);
                                Intent intent = new Intent(CreateTravelActivity.this, ContentActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ConstantTool.TRAVEL, travel);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            if (mDlg.isShowing()) {
                                mDlg.cancel();
                            }
                            if (!call.isCanceled()) {
                                ToastTool.show(CreateTravelActivity.this, "服务器出现问题: " + t.getMessage());
                            }
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){
        travelToolbar.setTitle("");
        travelToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(travelToolbar);
        travelToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
