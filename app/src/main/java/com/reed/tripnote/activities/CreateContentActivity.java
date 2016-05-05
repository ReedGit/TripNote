package com.reed.tripnote.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.reed.tripnote.R;
import com.reed.tripnote.adapters.AddImageAdapter;
import com.reed.tripnote.tools.CalculateTool;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;
import com.reed.tripnote.tools.LogTool;
import com.reed.tripnote.tools.RetrofitTool;
import com.reed.tripnote.tools.ToastTool;
import com.wq.photo.widget.PickConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateContentActivity extends AppCompatActivity {

    private static final String TAG = CreateContentActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    public Toolbar createToolbar;
    @Bind(R.id.tv_create_location)
    public TextView locationTV;
    @Bind(R.id.et_create_article)
    public EditText articleET;
    @Bind(R.id.rcv_create_image)
    public RecyclerView imageRCV;
    private AddImageAdapter mAdapter;
    private String coordinate;
    private String location;
    private String travelName;
    private long travelId;
    private String article;
    private Date createTime;
    private List<String> paths = new ArrayList<>();
    private ProgressDialog mDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_content);
        ButterKnife.bind(this);
        travelName = getIntent().getStringExtra(ConstantTool.TRAVEL_NAME);
        coordinate = getIntent().getStringExtra(ConstantTool.COORDINATE);
        location = getIntent().getStringExtra(ConstantTool.LOCATION);
        travelId = getIntent().getLongExtra(ConstantTool.TRAVEL_ID, -1);
        createTime = FormatTool.transformToDate(getIntent().getStringExtra(ConstantTool.CREATE_TIME));
        LogTool.i(TAG, "创建时间=====>>>>" + createTime + ", object====>" + (createTime == null));
        initView();
        initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            //在data中返回 选择的图片列表
            paths.addAll(data.getStringArrayListExtra("data"));
            mAdapter.setImagePath(paths);
            mAdapter.notifyDataSetChanged();
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
                article = articleET.getText().toString();
                if (!TextUtils.isEmpty(article)) {
                    uploadData();
                } else {
                    ToastTool.show("请填点内容吧~");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        if (TextUtils.isEmpty(travelName)) {
            createToolbar.setTitle("");
        } else {
            createToolbar.setTitle(travelName);
        }
        locationTV.setText(location);
        createToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(createToolbar);
        mAdapter = new AddImageAdapter();
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        imageRCV.setLayoutManager(manager);
        imageRCV.setAdapter(mAdapter);
        imageRCV.setItemAnimator(new DefaultItemAnimator());
    }

    private void initListener() {
        createToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter.setOnAddClickListener(new AddImageAdapter.OnAddClickListener() {
            @Override
            public void onAddClick(View view, int position) {
                new PickConfig.Builder(CreateContentActivity.this)
                        .maxPickSize(6 - paths.size())//最多选择几张
                        .isneedcamera(true)//是否需要第一项是相机
                        .spanCount(3)//一行显示几张照片
                        .isneedcrop(false)//受否需要剪裁
                        .pickMode(PickConfig.MODE_MULTIP_PICK)//单选还是多选
                        .build();
            }
        });
        mAdapter.setOnClearClickListener(new AddImageAdapter.OnClearClickListener() {
            @Override
            public void onClearClick(View view, int position) {
                paths.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });

    }

    private void uploadData() {
        Map<String, Object> map = new HashMap<>();
        map.put(ConstantTool.ARTICLE, article);
        map.put(ConstantTool.TRAVEL_ID, travelId);
        map.put(ConstantTool.COORDINATE, coordinate);
        map.put(ConstantTool.LOCATION, location);
        map.put(ConstantTool.DAY, CalculateTool.calculateDay(createTime, new Date()));
        Call<JSONObject> call = RetrofitTool.getService().createContent(map, filesToMultipartBodyParts());
        mDlg = ProgressDialog.show(CreateContentActivity.this, null, "上传中......", true);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                mDlg.cancel();
                if (response.code() != 200) {
                    ToastTool.show(response.message());
                    LogTool.e(TAG, "请求出错：" + response.message());
                    return;
                }
                LogTool.i(TAG, response.body().toString());
                JSONObject result = response.body();
                try {
                    if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                        ToastTool.show(result.getString(ConstantTool.MSG));
                        return;
                    }
                    ToastTool.show("上传成功");
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
                    ToastTool.show("服务器出现问题: " + t.getMessage());
                }
            }
        });
    }

    public Map<String, RequestBody> filesToMultipartBodyParts() {
        Map<String, RequestBody> imageMap = new HashMap<>();
        for (String path : paths) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), new File(path));
            imageMap.put("image\";filename=\""+ new File(path).getName(), requestBody);
        }
        return imageMap;
    }
}
