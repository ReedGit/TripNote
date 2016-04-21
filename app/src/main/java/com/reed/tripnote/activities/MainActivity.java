package com.reed.tripnote.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.reed.tripnote.R;
import com.reed.tripnote.App;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.fragments.AboutFragment;
import com.reed.tripnote.fragments.CollectionFragment;
import com.reed.tripnote.fragments.HomeFragment;
import com.reed.tripnote.fragments.TravelFragment;
import com.reed.tripnote.tools.ConstantTool;
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

    @Bind(R.id.toolbar)
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
        App app = (App) getApplication();
        user = app.getUser();
        if (user != null) {
            nameTV.setText(TextUtils.isEmpty(user.getNickName()) ? "" : user.getNickName());
            if (!TextUtils.isEmpty(user.getHeadImage())) {
                Glide.with(this)
                        .load(ConstantTool.imageUrl + user.getHeadImage())
                        .placeholder(R.mipmap.default_head)
                        .into(headCIV);
            }
        } else {
            nameTV.setText("登录/注册");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        setupSearchView(menu);
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
                if (user == null) {
                    ToastTool.show(R.string.please_login);
                    intentToLogin();
                } else {
                    Intent intent = new Intent(MainActivity.this, CreateTravelActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastTool.show("再按一次退出程序");
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
                    ToastTool.show(R.string.please_login);
                    intentToLogin();
                } else {
                    item.setChecked(true);
                    mainToolbar.setTitle(R.string.trip_note);
                    manager.beginTransaction().replace(R.id.main_frame, travelFragment).commit();
                }
                break;
            case R.id.drawer_collection:
                if (user == null) {
                    ToastTool.show(R.string.please_login);
                    intentToLogin();
                } else {
                    item.setChecked(true);
                    mainToolbar.setTitle(R.string.collection);
                    manager.beginTransaction().replace(R.id.main_frame, collectionFragment).commit();
                }
                break;
            case R.id.drawer_about:
                item.setChecked(true);
                mainToolbar.setTitle(R.string.about);
                manager.beginTransaction().replace(R.id.main_frame, aboutFragment).commit();
                break;
            case R.id.drawer_setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
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
                    Intent intent = new Intent(MainActivity.this, InformationActivity.class);
                    intent.putExtra(ConstantTool.USER_ID, user.getUserId());
                    startActivity(intent);
                } else {
                    intentToLogin();
                }
                break;
        }
    }

    private void initView() {
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

    private void initFragment() {
        homeFragment = new HomeFragment();
        travelFragment = new TravelFragment();
        aboutFragment = new AboutFragment();
        collectionFragment = new CollectionFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_frame, homeFragment).commit();
    }

    private void setupSearchView(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) searchMenuItem
                .getActionView();
        searchView.setIconifiedByDefault(true);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView
                    .setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                searchMenuItem.collapseActionView();
                                searchView.onActionViewCollapsed();
                            }
                        }
                    });

            searchView
                    .setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            searchView.setVisibility(View.INVISIBLE);
                            searchView.setVisibility(View.VISIBLE);
                            return false;
                        }
                        @Override
                        public boolean onQueryTextChange(String newText) {
                            return false;
                        }
                    });
        }
    }

}
