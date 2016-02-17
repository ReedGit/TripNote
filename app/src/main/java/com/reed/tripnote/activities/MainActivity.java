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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private NavigationView drawerNGV;
    private TextView nameTV;
    private CircleImageView headCIV;
    private UserBean user;
    private DrawerLayout mainDrawerLayout;
    private long exitTime;
    private Toolbar mainToolbar;
    private LinearLayout drawerLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();



        /*AMapLocationClient mLocationClient;
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        aMapLocation.getLatitude();//获取纬度
                        aMapLocation.getLongitude();//获取经度
                        aMapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(aMapLocation.getTime());
                        df.format(date);//定位时间
                        aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
                        aMapLocation.getCountry();//国家信息
                        aMapLocation.getProvince();//省信息
                        aMapLocation.getCity();//城市信息
                        aMapLocation.getDistrict();//城区信息
                        aMapLocation.getRoad();//街道信息
                        aMapLocation.getCityCode();//城市编码
                        aMapLocation.getAdCode();//地区编码
                        Log.i("info",aMapLocation.getLatitude()+","+aMapLocation.getLongitude()+" "+aMapLocation.getAddress());
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(mLocationListener);

        AMapLocationClientOption aMapLocationClientOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        aMapLocationClientOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        aMapLocationClientOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        aMapLocationClientOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        aMapLocationClientOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        aMapLocationClientOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(aMapLocationClientOption);
        //启动定位
        mLocationClient.startLocation();*/

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
                Toast.makeText(MainActivity.this,"新建游记",Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(MainActivity.this,PersonalActivity.class);
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
        mainDrawerLayout = (DrawerLayout) findViewById(R.id.dl_main);
        mainToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        mainToolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(mainToolbar);
        drawerNGV = (NavigationView) findViewById(R.id.navigationView);
        View drawerHeader = drawerNGV.inflateHeaderView(R.layout.drawer_header);
        drawerLL = (LinearLayout) drawerHeader.findViewById(R.id.ll_drawer);
        nameTV = (TextView) drawerHeader.findViewById(R.id.tv_user_name);
        headCIV = (CircleImageView) drawerHeader.findViewById(R.id.civ_user_head);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mainDrawerLayout, mainToolbar, R.string.open, R.string.close);
        drawerToggle.syncState();
        mainDrawerLayout.setDrawerListener(drawerToggle);

    }

    /**
     * 跳转登录界面
     */
    private void intentToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void initListener(){
        drawerNGV.setNavigationItemSelectedListener(this);
        drawerLL.setOnClickListener(this);
    }

}
