package com.reed.tripnote.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.reed.tripnote.App;
import com.reed.tripnote.R;
import com.reed.tripnote.beans.TravelBean;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;
import com.reed.tripnote.tools.LogTool;
import com.reed.tripnote.tools.PathUtil;
import com.reed.tripnote.tools.ProviderPathUtil;
import com.reed.tripnote.tools.RetrofitTool;
import com.reed.tripnote.tools.ToastTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTravelActivity extends AppCompatActivity {

    private static final String TAG = CreateTravelActivity.class.getSimpleName();
    public static final int GALLERY_REQUEST = 0;
    public static final int CAMERA_REQUEST = 1;

    @Bind(R.id.toolbar)
    public Toolbar travelToolbar;
    @Bind(R.id.et_travel_title)
    public EditText titleET;
    @Bind(R.id.et_travel_introduction)
    public EditText introductionET;
    @Bind(R.id.iv_travel_image)
    public ImageView coverImageView;
    private UserBean user;
    private ProgressDialog mDlg;
    private Call<JSONObject> call;
    private String photoPath;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                    if (data == null) {
                        LogTool.i(TAG, "return intent is null");
                        return;
                    }
                    Uri uri = data.getData();
                    photoPath = ProviderPathUtil.getPath(CreateTravelActivity.this, uri);
                case CAMERA_REQUEST:
                    Glide.with(CreateTravelActivity.this).load("file://" + photoPath).into(coverImageView);
                    break;
            }
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
                    Map<String, RequestBody> coverMap = new HashMap<>();
                    if (!TextUtils.isEmpty(photoPath)) {
                        RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(photoPath));
                        coverMap.put("coverImage\";filename=\"coverImage.jpg", file);
                    }
                    call = RetrofitTool.getService().createTravel(map, coverMap);
                    mDlg = ProgressDialog.show(CreateTravelActivity.this, null, "请稍后......", true);
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
                                ToastTool.show("服务器出现问题: " + t.getMessage());
                            }
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        travelToolbar.setTitle(R.string.create_note);
        travelToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(travelToolbar);
        travelToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        coverImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateTravelActivity.this);
                String[] items = new String[]{"拍照", "手机相册"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            selectImageFromCamera();
                        } else if (which == 1) {
                            selectImageFromLocal();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    //从本地相册获取图片
    private void selectImageFromLocal() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择图片"), GALLERY_REQUEST);
    }

    //拍照获取图片
    private void selectImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoPath = PathUtil.getTakePhotoPath(this);
        Uri imageUri = Uri.fromFile(new File(photoPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }
}
