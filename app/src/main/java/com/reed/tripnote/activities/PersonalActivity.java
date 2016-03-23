package com.reed.tripnote.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.reed.tripnote.App;
import com.reed.tripnote.R;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.controller.UserManager;
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
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户个人信息修改界面
 * Created by 伟 on 2016/2/15.
 */
public class PersonalActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PersonalActivity.class.toString();

    public static final int GALLERY_REQUEST = 0;
    public static final int CAMERA_REQUEST = 1;

    @Bind(R.id.toolbar)
    public Toolbar personalToolbar;

    @Bind(R.id.civ_personal_head)
    public CircleImageView headCIV;

    @Bind(R.id.et_personal_introduction)
    public EditText introductionEt;

    @Bind(R.id.et_personal_nickname)
    public EditText nicknameEt;

    @Bind(R.id.et_personal_email)
    public EditText emailEt;

    @Bind(R.id.rb_personal_male)
    public RadioButton maleRB;

    @Bind(R.id.rb_personal_female)
    public RadioButton femaleRB;

    @Bind(R.id.btn_personal_modify)
    public Button modifyBtn;

    private boolean isEditable = false;
    private UserBean user;

    private ProgressDialog mDlg;

    private String email;
    private String introduction;
    private String nickName;
    private int sex;
    private String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        user = ((App) getApplication()).getUser();
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        if (TextUtils.isEmpty(user.getHeadImage())) {
            Glide.with(this).load(ConstantTool.baseUrl + user.getHeadImage()).into(headCIV);
        }
        super.onResume();
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
                    photoPath = ProviderPathUtil.getPath(PersonalActivity.this, uri);
                case CAMERA_REQUEST:
                    uploadImage(photoPath);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify_information:
                if (!isEditable) {
                    isEditable = true;
                    item.setTitle(R.string.complete);
                    item.setIcon(R.mipmap.create);
                    introductionEt.setEnabled(true);
                    emailEt.setEnabled(true);
                    nicknameEt.setEnabled(true);
                    maleRB.setEnabled(true);
                    femaleRB.setEnabled(true);
                } else {
                    email = emailEt.getText().toString().trim();
                    nickName = nicknameEt.getText().toString().trim();
                    introduction = introductionEt.getText().toString().trim();
                    if (TextUtils.isEmpty(email)) {
                        ToastTool.show(PersonalActivity.this, "邮箱不能为空");
                        return false;
                    }
                    if (!FormatTool.isEmail(email)) {
                        ToastTool.show(PersonalActivity.this, "邮箱格式不正确");
                        return false;
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put(ConstantTool.EMAIL, email);
                    if (!TextUtils.isEmpty(introduction)) {
                        map.put(ConstantTool.INTRODUCTION, introduction);
                    }
                    if (!TextUtils.isEmpty(nickName)) {
                        map.put(ConstantTool.NICKNAME, nickName);
                    }
                    if (maleRB.isChecked()) {
                        sex = UserBean.MALE;
                    } else {
                        sex = UserBean.FEMALE;
                    }
                    map.put(ConstantTool.SEX, sex);
                    map.put(ConstantTool.TOKEN, user.getToken());
                    Call<JSONObject> call = RetrofitTool.getService().setProfile(user.getUserId(), map);
                    mDlg = ProgressDialog.show(PersonalActivity.this, null, "修改中......", true);
                    call.enqueue(new Callback<JSONObject>() {
                        @Override
                        public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                            if (response.code() != 200) {
                                ToastTool.show(PersonalActivity.this, response.message());
                                LogTool.e(TAG, "请求出错：" + response.message());
                                return;
                            }
                            LogTool.i(TAG, response.body().toString());
                            JSONObject result = response.body();
                            try {
                                if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                                    ToastTool.show(PersonalActivity.this, result.getString(ConstantTool.MSG));
                                    return;
                                }
                                isEditable = false;
                                item.setTitle(R.string.modify_personal);
                                item.setIcon(R.mipmap.create);
                                introductionEt.setEnabled(false);
                                emailEt.setEnabled(false);
                                nicknameEt.setEnabled(false);
                                maleRB.setEnabled(false);
                                femaleRB.setEnabled(false);
                                user.setSex(sex);
                                user.setIntroduction(introduction);
                                user.setNickName(nickName);
                                user.setEmail(email);
                                UserManager.loginUser(PersonalActivity.this, user);
                                ToastTool.show(PersonalActivity.this, "修改成功");
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<JSONObject> call, Throwable t) {
                            mDlg.cancel();
                            LogTool.e(TAG, t.getMessage());
                            ToastTool.show(PersonalActivity.this, t.getMessage());
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_personal_modify:
                Intent intent = new Intent(PersonalActivity.this, ModifyPassword.class);
                startActivity(intent);
                break;
            case R.id.civ_personal_head:
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
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
                break;
        }
    }

    private void initView() {
        personalToolbar.setTitle(R.string.personal);
        personalToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(personalToolbar);
        introductionEt.setText(TextUtils.isEmpty(user.getIntroduction()) ? "" : user.getIntroduction());
        nicknameEt.setText(TextUtils.isEmpty(user.getNickName()) ? "" : user.getNickName());
        emailEt.setText(user.getEmail());
        switch (user.getSex()) {
            case UserBean.MALE:
                maleRB.setChecked(true);
                femaleRB.setChecked(false);
                break;
            case UserBean.FEMALE:
                femaleRB.setChecked(true);
                maleRB.setChecked(false);
                break;
        }
        modifyBtn.setOnClickListener(this);
        personalToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void uploadImage(String path) {

    }

}
