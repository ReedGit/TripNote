package com.reed.tripnote.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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
import com.reed.tripnote.fragments.AboutFragment;
import com.reed.tripnote.fragments.CollectionFragment;
import com.reed.tripnote.fragments.HomeFragment;
import com.reed.tripnote.fragments.TravelFragment;
import com.reed.tripnote.tools.ToastTool;

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

    private AboutFragment aboutFragment;
    private HomeFragment homeFragment;
    private CollectionFragment collectionFragment;
    private TravelFragment travelFragment;
    private FragmentManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initListener();
        initFragment();

    }

    @Override
    protected void onResume() {
        super.onResume();
        TripNoteApplication app = (TripNoteApplication) getApplication();
        user = app.getUser();
        if (user != null) {
            nameTV.setText(user.getEmail());
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
                ToastTool.show(MainActivity.this, "再按一次退出程序");
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
                mainToolbar.setTitle(R.string.main_page);
                manager.beginTransaction().replace(R.id.main_frame, homeFragment).commit();
                break;
            case R.id.drawer_trip_note:
                if (user == null) {
                    ToastTool.show(MainActivity.this, R.string.please_login);
                    intentToLogin();
                } else {
                    item.setChecked(true);
                    mainToolbar.setTitle(R.string.trip_note);
                    manager.beginTransaction().replace(R.id.main_frame, travelFragment).commit();
                }
                break;
            case R.id.drawer_collection:
                if (user == null) {
                    ToastTool.show(MainActivity.this, R.string.please_login);
                    intentToLogin();
                } else {
                    item.setChecked(true);
                    mainToolbar.setTitle(R.string.collection);
                    manager.beginTransaction().replace(R.id.main_frame, collectionFragment).commit();
                }
                break;
            /*case R.id.drawer_liked:
                if (user == null) {
                    Toast.makeText(MainActivity.this, "请先登录账号", Toast.LENGTH_SHORT).show();
                    intentToLogin();
                } else {
                    item.setChecked(true);
                    mainToolbar.setTitle(R.string.liked);
                }
                break;
            case R.id.drawer_commented:
                if (user == null) {
                    Toast.makeText(MainActivity.this, "请先登录账号", Toast.LENGTH_SHORT).show();
                    intentToLogin();
                } else {
                    item.setChecked(true);
                    mainToolbar.setTitle(R.string.commented);
                }
                break;*/
            case R.id.drawer_about:
                item.setChecked(true);
                mainToolbar.setTitle(R.string.about);
                manager.beginTransaction().replace(R.id.main_frame, aboutFragment).commit();
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
        mainToolbar.setTitle(R.string.main_page);
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

    private void initFragment(){
        homeFragment = new HomeFragment();
        travelFragment = new TravelFragment();
        aboutFragment = new AboutFragment();
        collectionFragment = new CollectionFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, homeFragment).commit();
    }

}
