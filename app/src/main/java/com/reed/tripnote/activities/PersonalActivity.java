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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 伟 on 2016/2/15.
 */
public class PersonalActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar personalToolbar;
    private CircleImageView headCIV;
    private EditText introductionEt;
    private EditText nicknameEt;
    private EditText emailEt;
    private RadioGroup sexRG;
    private RadioButton maleRB;
    private RadioButton femaleRB;
    private RadioButton invisibleRB;
    private Button modifyBtn;
    private boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
        switch (v.getId()){
            case R.id.btn_personal_modify:
                break;
        }
    }

    private void initView(){
        personalToolbar = (Toolbar) findViewById(R.id.toolbar_personal);
        personalToolbar.setTitle(R.string.personal);
        personalToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(personalToolbar);
        headCIV = (CircleImageView) findViewById(R.id.civ_personal_head);
        introductionEt = (EditText) findViewById(R.id.et_personal_introduction);
        nicknameEt = (EditText) findViewById(R.id.et_personal_nickname);
        emailEt = (EditText) findViewById(R.id.et_personal_email);
        sexRG = (RadioGroup) findViewById(R.id.rg_personal_sex);
        maleRB = (RadioButton) findViewById(R.id.rb_personal_male);
        femaleRB = (RadioButton) findViewById(R.id.rb_personal_female);
        invisibleRB = (RadioButton) findViewById(R.id.rb_personal_invisible);
        modifyBtn = (Button) findViewById(R.id.btn_personal_modify);
        modifyBtn.setOnClickListener(this);
        personalToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
