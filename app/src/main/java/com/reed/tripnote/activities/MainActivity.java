package com.reed.tripnote.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reed.tripnote.R;
import com.reed.tripnote.TripNoteApplication;
import com.reed.tripnote.beans.UserBean;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @Bind(R.id.navigationView)
    public NavigationView drawerNGV;

    public TextView nameTV;

    public CircleImageView headCIV;

    public UserBean user;

    @Bind(R.id.dl_main)
    public DrawerLayout mainDrawerLayout;

    public long exitTime;

    @Bind(R.id.toolbar_main)
    public Toolbar mainToolbar;

    public LinearLayout drawerLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        TripNoteApplication app = (TripNoteApplication) getApplication();
        user = app.getUser();
        if (user != null) {
            nameTV.setText(user.getUsername());
        } else {
            nameTV.setText("登录/注册");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //主界面左上角的icon点击反应
                mainDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.create_note:
                /*if (user == null){
                    intentToLogin();
                } else {
                    Toast.makeText(MainActivity.this, "新建游记", Toast.LENGTH_SHORT).show();
                }*/
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mainDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.drawer_main_page:
                item.setChecked(true);
                break;
            case R.id.drawer_trip_note:
                if (user == null) {
                    Toast.makeText(MainActivity.this, " 请先登录账号", Toast.LENGTH_SHORT).show();
                    intentToLogin();
                } else {
                    item.setChecked(true);
                }
                break;
            case R.id.drawer_collection:
                if (user == null) {
                    Toast.makeText(MainActivity.this, " 请先登录账号", Toast.LENGTH_SHORT).show();
                    intentToLogin();
                } else {
                    item.setChecked(true);
                }
                break;
            case R.id.drawer_liked:
                if (user == null) {
                    Toast.makeText(MainActivity.this, "请先登录账号", Toast.LENGTH_SHORT).show();
                    intentToLogin();
                } else {
                    item.setChecked(true);
                }
                break;
            case R.id.drawer_commented:
                if (user == null) {
                    Toast.makeText(MainActivity.this, "请先登录账号", Toast.LENGTH_SHORT).show();
                    intentToLogin();
                } else {
                    item.setChecked(true);
                }
                break;
            case R.id.drawer_about:
                item.setChecked(true);
                Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_drawer:
                if (user != null) {
                    Toast.makeText(MainActivity.this, "跳转个人资料界面", Toast.LENGTH_SHORT).show();
                } else {
                    intentToLogin();
                }
                break;
        }
    }

    private void initView() {
        mainToolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(mainToolbar);
        View drawerHeader = drawerNGV.inflateHeaderView(R.layout.drawer_header);
        drawerLL = (LinearLayout) drawerHeader.findViewById(R.id.ll_drawer);
        nameTV = (TextView) drawerHeader.findViewById(R.id.tv_user_name);
        headCIV = (CircleImageView) drawerHeader.findViewById(R.id.civ_user_head);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mainDrawerLayout, mainToolbar, R.string.open, R.string.close);
        drawerToggle.syncState();
        mainDrawerLayout.addDrawerListener(drawerToggle);

    }

    /**
     * 跳转登录界面
     */
    private void intentToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void initListener() {
        drawerNGV.setNavigationItemSelectedListener(this);
        drawerLL.setOnClickListener(this);
    }

}
