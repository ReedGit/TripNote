package com.reed.tripnote.activities;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.reflect.TypeToken;
import com.reed.tripnote.App;
import com.reed.tripnote.R;
import com.reed.tripnote.beans.ContentBean;
import com.reed.tripnote.beans.TravelBean;
import com.reed.tripnote.beans.UserBean;
import com.reed.tripnote.tools.ConstantTool;
import com.reed.tripnote.tools.FormatTool;
import com.reed.tripnote.tools.LogTool;
import com.reed.tripnote.tools.RetrofitTool;
import com.reed.tripnote.tools.ToastTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 游记内容详情
 * Created by 伟 on 2016/3/12.
 */
public class ContentActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener, AMap.OnMarkerClickListener, View.OnClickListener {

    private static final String TAG = ContentActivity.class.getSimpleName();

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
    private List<ContentBean> contents = new ArrayList<>();
    private List<LatLng> lats = new ArrayList<>();
    private Call<JSONObject> contentCall;
    private boolean isLike = false;
    private boolean isCollect = false;
    private int likeNum = 0;
    private int collectNum = 0;
    private int commentNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        travel = (TravelBean) getIntent().getSerializableExtra(ConstantTool.TRAVEL);
        user = ((App) getApplication()).getUser();
        contentMap.onCreate(savedInstanceState);
        likeNum = travel.getLiked();
        collectNum = travel.getCollection();
        commentNum = travel.getComment();
        likeFAB.setLabelText(String.valueOf(likeNum));
        collectionFAB.setLabelText(String.valueOf(collectNum));
        commentFAB.setLabelText(String.valueOf(commentNum));
        init();
    }

    @Override
    protected void onResume() {
        getData();
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
    protected void onStop() {
        super.onStop();
        if (contentCall != null && contentCall.isExecuted()) {
            contentCall.cancel();
        }
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
        if (travel != null) {
            getMenuInflater().inflate(R.menu.travel_menu, menu);
            if (user != null && user.getUserId() == travel.getUserId() && travel.getEndTime() == null) {
                menu.getItem(1).setVisible(true);
                menu.getItem(2).setVisible(true);
            } else {
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(false);
            }
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
                intent.putExtra(ConstantTool.CREATE_TIME, FormatTool.transformToString(travel.getStartTime()));
                intent.putExtra(ConstantTool.TRAVEL_ID, travel.getTravelId());
                startActivity(intent);
                break;
            case R.id.information:
                Dialog dialog = new Dialog(ContentActivity.this);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setTitle("游记简介");
                View view = LayoutInflater.from(ContentActivity.this).inflate(R.layout.dlg_travel, null, false);
                TextView introductionTV = (TextView) view.findViewById(R.id.tv_travel_introduction);
                String introduction;
                if (TextUtils.isEmpty(travel.getIntroduction())) {
                    introduction = "对不起，该游记没有提供简介";
                } else {
                    introduction = travel.getIntroduction();
                }
                introductionTV.setText(introduction);
                dialog.setContentView(view);
                dialog.show();
                break;
            case R.id.finish:
                finishTravel();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_content_collection:
                if (user == null) {
                    Intent collectIntent = new Intent(ContentActivity.this, LoginActivity.class);
                    startActivity(collectIntent);
                } else {
                    if (isCollect) {
                        collectionFAB.setImageResource(R.drawable.ic_favorite_outline_24dp);
                        collectionFAB.setLabelText(String.valueOf(collectNum - 1));
                        doCancelCollect();
                    } else {
                        collectionFAB.setImageResource(R.drawable.ic_favorite_24dp);
                        collectionFAB.setLabelText(String.valueOf(collectNum + 1));
                        doCollection();
                    }
                }
                break;
            case R.id.fab_content_comment:
                contentFAM.close(true);
                Intent intent = new Intent(ContentActivity.this, CommentActivity.class);
                intent.putExtra(ConstantTool.TRAVEL_ID, travel.getTravelId());
                startActivity(intent);
                break;
            case R.id.fab_content_liked:
                if (user == null) {
                    Intent likeIntent = new Intent(ContentActivity.this, LoginActivity.class);
                    startActivity(likeIntent);
                } else {
                    if (isLike) {
                        likeFAB.setImageResource(R.drawable.ic_like_24dp);
                        likeFAB.setLabelText(String.valueOf(likeNum - 1));
                        doCancelLike();
                    } else {
                        likeFAB.setImageResource(R.drawable.ic_liked_24dp);
                        likeFAB.setLabelText(String.valueOf(likeNum + 1));
                        doLiked();
                    }
                }
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
        if (aMap != null && marker.getObject() != null) {
            Intent intent = new Intent(ContentActivity.this, ContentDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConstantTool.CONTENT, (ContentBean) marker.getObject());
            intent.putExtra(ConstantTool.TRAVEL_NAME, travel.getTitle());
            intent.putExtras(bundle);
            startActivity(intent);

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
     * 在地图上添加marker
     */
    private void addMarkersToMap(LatLng latLng, ContentBean contentBean) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title("第" + contentBean.getDay() + "天");
        options.draggable(true);
        options.icon(BitmapDescriptorFactory.defaultMarker());
        // 将Marker设置为贴地显示，可以双指下拉看效果
        options.setFlat(true);
        Marker marker = aMap.addMarker(options);
        marker.setObject(contentBean);
        marker.showInfoWindow();
    }

    private void getData() {
        contentCall = RetrofitTool.getService().getContent(travel.getTravelId());
        contentCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (successReq(response)) {
                    JSONObject result = response.body();
                    LogTool.i(TAG, "结果：=====>>>>" + result.toString());
                    try {
                        contents = FormatTool.gson.fromJson(String.valueOf(result.getJSONArray(ConstantTool.CONTENT)), new TypeToken<List<ContentBean>>() {
                        }.getType());
                        lats.clear();
                        for (ContentBean content : contents) {
                            double latitude = Double.parseDouble(content.getCoordinate().split(",")[0].trim());
                            double longitude = Double.parseDouble(content.getCoordinate().split(",")[1].trim());
                            LatLng lat = new LatLng(latitude, longitude);
                            addMarkersToMap(lat, content);
                            lats.add(lat);
                        }
                        aMap.addPolyline((new PolylineOptions())
                                .addAll(lats)
                                .width(5)
                                .setDottedLine(false)
                                .geodesic(true)
                                .color(Color.BLACK)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
        Map<String, Object> map = new HashMap<>();
        if (user != null) {
            map.put("userId", user.getUserId());
        }
        Call<JSONObject> countCall = RetrofitTool.getService().getCount(travel.getTravelId(), map);
        countCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (successReq(response)) {
                    JSONObject result = response.body();
                    try {
                        JSONObject data = result.getJSONObject(ConstantTool.DATA);
                        commentNum = data.getInt("comments");
                        likeNum = data.getInt("likes");
                        collectNum = data.getInt("collections");
                        if (result.has("addition")) {
                            JSONObject addition = result.getJSONObject(ConstantTool.ADDITION);
                            isCollect = addition.getBoolean("isCollect");
                            isLike = addition.getBoolean("isLike");
                        }
                        collectionFAB.setImageResource(isCollect ? R.drawable.ic_favorite_24dp : R.drawable.ic_favorite_outline_24dp);
                        likeFAB.setImageResource(isLike ? R.drawable.ic_liked_24dp : R.drawable.ic_like_24dp);
                        collectionFAB.setLabelText(String.valueOf(collectNum));
                        likeFAB.setLabelText(String.valueOf(likeNum));
                        commentFAB.setLabelText(String.valueOf(commentNum));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });

    }

    private void doLiked() {
        Call<JSONObject> call = RetrofitTool.getService().like(travel.getTravelId(), user.getUserId());
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (!successReq(response)) {
                    ToastTool.show("点赞失败");
                    likeFAB.setImageResource(R.drawable.ic_like_24dp);
                    likeFAB.setLabelText(String.valueOf(likeNum - 1));
                } else {
                    isLike = true;
                    likeNum++;
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
    }

    private void doCollection() {
        Call<JSONObject> call = RetrofitTool.getService().collect(travel.getTravelId(), user.getUserId());
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (!successReq(response)) {
                    ToastTool.show("收藏失败");
                    collectionFAB.setImageResource(R.drawable.ic_favorite_outline_24dp);
                    collectionFAB.setLabelText(String.valueOf(collectNum - 1));
                } else {
                    isCollect = true;
                    collectNum++;
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
    }

    private void doCancelLike() {
        Call<JSONObject> call = RetrofitTool.getService().cancelLike(travel.getTravelId(), user.getUserId());
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (!successReq(response)) {
                    ToastTool.show("取消失败");
                    likeFAB.setImageResource(R.drawable.ic_liked_24dp);
                    likeFAB.setLabelText(String.valueOf(likeNum + 1));
                } else {
                    isLike = false;
                    likeNum--;
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
    }

    private void doCancelCollect() {
        Call<JSONObject> call = RetrofitTool.getService().cancelCollect(travel.getTravelId(), user.getUserId());
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (!successReq(response)) {
                    ToastTool.show("取消失败");
                    collectionFAB.setImageResource(R.drawable.ic_favorite_24dp);
                    collectionFAB.setLabelText(String.valueOf(collectNum + 1));
                } else {
                    isCollect = false;
                    collectNum--;
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
    }

    private boolean successReq(Response<JSONObject> response) {
        if (response.code() != 200) {
            ToastTool.show(response.message());
            LogTool.e(TAG, response.message());
            return false;
        }
        JSONObject result = response.body();
        try {
            if (result.getInt(ConstantTool.CODE) != ConstantTool.RESULT_OK) {
                ToastTool.show(result.getString(ConstantTool.MSG));
                return false;
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void finishTravel() {
        Call<JSONObject> call = RetrofitTool.getService().finishTravel(travel.getTravelId());
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (successReq(response)) {
                    ToastTool.show("行程已结束");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
    }

}
