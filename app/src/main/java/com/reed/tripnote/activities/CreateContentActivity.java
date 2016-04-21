package com.reed.tripnote.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.reed.tripnote.R;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.ToastTool;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateContentActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    public Toolbar createToolbar;
    @Bind(R.id.tv_create_location)
    public TextView locationTV;
    @Bind(R.id.et_create_article)
    public EditText articleET;
    private String coordinate;
    private String location;
    private String travelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_content);
        ButterKnife.bind(this);
        travelName = getIntent().getStringExtra(ConstantTool.TRAVEL_NAME);
        coordinate = getIntent().getStringExtra(ConstantTool.COORDINATE);
        location = getIntent().getStringExtra(ConstantTool.LOCATION);
        initView();
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
                ToastTool.show("发布成功");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){
        if (TextUtils.isEmpty(travelName)) {
            createToolbar.setTitle("");
        } else {
            createToolbar.setTitle(travelName);
        }
        locationTV.setText(location);
        createToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(createToolbar);
        createToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
