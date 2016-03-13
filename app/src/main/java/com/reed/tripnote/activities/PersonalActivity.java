package com.reed.tripnote.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.reed.tripnote.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 用户个人信息修改界面
 * Created by 伟 on 2016/2/15.
 */
public class PersonalActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.toolbar_personal)
    public Toolbar personalToolbar;

    @Bind(R.id.civ_personal_head)
    public CircleImageView headCIV;

    @Bind(R.id.et_personal_introduction)
    public EditText introductionEt;

    @Bind(R.id.et_personal_nickname)
    public EditText nicknameEt;

    @Bind(R.id.et_personal_email)
    public EditText emailEt;

    @Bind(R.id.rg_personal_sex)
    public RadioGroup sexRG;

    @Bind(R.id.rb_personal_male)
    public RadioButton maleRB;

    @Bind(R.id.rb_personal_female)
    public RadioButton femaleRB;

    @Bind(R.id.rb_personal_invisible)
    public RadioButton invisibleRB;

    @Bind(R.id.btn_personal_modify)
    public Button modifyBtn;

    private boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        initView();
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
                if (!isEditable) {
                    isEditable = true;
                    item.setTitle(R.string.complete);
                    item.setIcon(R.mipmap.create);
                    introductionEt.setEnabled(true);
                    emailEt.setEnabled(true);
                    nicknameEt.setEnabled(true);
                    maleRB.setEnabled(true);
                    femaleRB.setEnabled(true);
                    invisibleRB.setEnabled(true);
                } else {
                    isEditable = false;
                    item.setTitle(R.string.modify_personal);
                    item.setIcon(R.mipmap.create);
                    introductionEt.setEnabled(false);
                    emailEt.setEnabled(false);
                    nicknameEt.setEnabled(false);
                    maleRB.setEnabled(false);
                    femaleRB.setEnabled(false);
                    invisibleRB.setEnabled(false);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_personal_modify:
                break;
        }
    }

    private void initView() {
        personalToolbar.setTitle(R.string.personal);
        personalToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(personalToolbar);
        modifyBtn.setOnClickListener(this);
        personalToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
