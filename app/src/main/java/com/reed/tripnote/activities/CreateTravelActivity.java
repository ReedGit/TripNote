package com.reed.tripnote.activities;

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

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateTravelActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    public Toolbar travelToolbar;
    @Bind(R.id.et_travel_title)
    public EditText titleET;
    private UserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_travel);
        ButterKnife.bind(this);
        user = ((App) getApplication()).getUser();
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
                String title = titleET.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    titleET.setError("请输入游记名称");
                } else {
                    TravelBean travel = new TravelBean();
                    travel.setTitle(title);
                    travel.setUserId(user.getUserId());
                    Intent intent = new Intent(CreateTravelActivity.this, ContentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ConstantTool.TRAVEL, travel);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
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
