package com.reed.tripnote.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.reed.tripnote.App;
import com.reed.tripnote.R;
import com.reed.tripnote.beans.TravelBean;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.LogTool;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 游记内容详情
 * Created by 伟 on 2016/3/12.
 */
public class ContentActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener, AMap.OnMarkerClickListener, View.OnClickListener {

    private static final String TAG = ContentActivity.class.toString();

    @Bind(R.id.toolbar)
    public Toolbar contentToolbar;

    @Bind(R.id.map_content)
    public MapView contentMap;

    @Bind(R.id.fab_content_liked)
    public FloatingActionButton likeFAB;
    @Bind(R.id.fab_content_comment)
    public FloatingActionButton commentFAB;
    @Bind(R.id.fab_content_collection)
    public FloatingActionButton collectionFAB;
    @Bind(R.id.fam_content)
    public FloatingActionMenu contentFAM;

    private AMap aMap;

    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private String address;
    private String coordinate;
    private TravelBean travel;
    private UserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        travel = (TravelBean) getIntent().getSerializableExtra(ConstantTool.TRAVEL);
        user = ((App) getApplication()).getUser();
        contentMap.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contentMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        contentMap.onPause();
        deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contentMap.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        contentMap.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (travel != null && user != null && user.getUserId() == travel.getUserId()) {
            getMenuInflater().inflate(R.menu.add_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(ContentActivity.this, CreateContentActivity.class);
                intent.putExtra(ConstantTool.TRAVEL_NAME, travel.getTitle());
                intent.putExtra(ConstantTool.COORDINATE, coordinate);
                intent.putExtra(ConstantTool.LOCATION, address);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_content_collection:
                break;
            case R.id.fab_content_comment:
                contentFAM.close(true);
                Intent intent = new Intent(ContentActivity.this, CommentActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_content_liked:
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                //定位成功回调信息，设置相关消息
                double latitude = aMapLocation.getLatitude();//获取纬度
                double longitude = aMapLocation.getLongitude();//获取经度
                coordinate = latitude + "," + longitude;
                address = aMapLocation.getCountry() + " " + aMapLocation.getProvince() + " " + aMapLocation.getCity();
                //addMarkersToMap(new LatLng(latitude, longitude), "测试");
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                LogTool.e(TAG, errText);
            }
        }
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            //设置定位监听
            mLocationClient.setLocationListener(this);
            AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
            //设置为高精度定位模式
            locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            locationClientOption.setNeedAddress(true);
            locationClientOption.setHttpTimeOut(10 * 1000);
            //设置定位参数
            mLocationClient.setLocationOption(locationClientOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();
        }
    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    //点击标记事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            //jumpPoint(marker, new LatLng(latitude, longitude));
        }
        return false;
    }

    private void init() {
        if (aMap == null) {
            aMap = contentMap.getMap();
            setUpMap();
        }
        contentToolbar.setTitle(travel.getTitle());
        contentToolbar.setNavigationIcon(R.mipmap.toolbar_back);
        setSupportActionBar(contentToolbar);
        contentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置一些aMap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.setOnMarkerClickListener(this);
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker, final LatLng latLng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection projection = aMap.getProjection();
        Point startPoint = projection.toScreenLocation(latLng);
        startPoint.offset(0, -100);
        final LatLng startLatLng = projection.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;

                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * latLng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * latLng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latLng, String text) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title(text);
        options.draggable(true);
        options.icon(BitmapDescriptorFactory.defaultMarker());
        // 将Marker设置为贴地显示，可以双指下拉看效果
        options.setFlat(true);
        Marker marker = aMap.addMarker(options);
        marker.showInfoWindow();
    }

}
