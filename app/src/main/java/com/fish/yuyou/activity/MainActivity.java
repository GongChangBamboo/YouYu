package com.fish.yuyou.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.fish.yuyou.R;
import com.fish.yuyou.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements AMapLocationListener {
    private final static String TAG = "MainActivity";

    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private UiSettings uiSettings;
    private LatLng latLng;
    private float rotateRangle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mapview.onCreate(savedInstanceState);
        setDefaultInit();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        if (aMap == null) {
            aMap = mapview.getMap();
        }
      /*  MAP_TYPE_NAVI 导航地图
        MAP_TYPE_NIGHT 夜景地图
        MAP_TYPE_NORMAL 白昼地图（即普通地图）
        MAP_TYPE_SATELLITE 卫星图*/
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        Log.i("TAG", "当前时间=" + hour + "点");
//        if (hour >= 6 && hour < 18) {
//            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
//        } else {
//            aMap.setMapType(AMap.MAP_TYPE_NIGHT);
//        }
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        uiSettings = aMap.getUiSettings();
        settingZoom();
        //是否显示指南针
        uiSettings.setCompassEnabled(false);
        //开启比例尺
        uiSettings.setScaleControlsEnabled(true);
        //  设置logo位置
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        //AMapOptions.LOGO_POSITION_BOTTOM_LEFTLOGO（左边）
        //AMapOptions.LOGO_MARGIN_BOTTOMLOGO（底部)
        //AMapOptions.LOGO_MARGIN_RIGHTLOGO（右边）
        //AMapOptions.LOGO_POSITION_BOTTOM_CENTER（地图底部居中）
        //AMapOptions.LOGO_POSITION_BOTTOM_LEFT（地图左下角）
        //AMapOptions.LOGO_POSITION_BOTTOM_RIGHT （地图右下角）
//        //显示默认的定位按钮
//        uiSettings.setMyLocationButtonEnabled(true);
//
//        // 可触发定位并显示当前位置
//        aMap.setMyLocationEnabled(true);

        //这里设置定位为了在点击定位按钮后，显示地图定位的位置方便查看
        //注意这里有个坑，在点击定位后发现定位到了默认的位置（海里面），造成这种情况并不是权限和代码的问题，
        //遇到这种情况时，需要手动将GPS定位打开就OK了
//        MyLocationStyle mls = new MyLocationStyle();
//        mls.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
//        aMap.setMyLocationEnabled(true);
//        aMap.setMyLocationStyle(mls);


        setGestures();
    }

    private void setGestures() {

        //开启放缩手势
        uiSettings.setZoomGesturesEnabled(true);
        //开启滑动手势
        uiSettings.setScrollGesturesEnabled(true);
        //开启旋转手势
        uiSettings.setRotateGesturesEnabled(false);
        //开启双指倾斜手势
        uiSettings.setTiltGesturesEnabled(false);
        //开启全部手势
//        uiSettings.setAllGesturesEnabled(true);


        //指定手势中心点
//        aMap.setPointToCenter(100,100);

        //开启中心为手势中心
        uiSettings.setGestureScaleByMapCenter(false);
    }

    private void settingZoom() {

        //是否允许显示地图缩放按钮
        uiSettings.setZoomControlsEnabled(false);
        //是否允许收拾手势缩放地图
        uiSettings.setZoomGesturesEnabled(true);
        //设置双击地图放大在地图中心位置放大，false则是在点击位置放大
        uiSettings.setZoomInByScreenCenter(true);
        //地图缩放按钮的位置
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM);
//        AMapOptions.ZOOM_POSITION_RIGHT_CENTER
//        AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM
        //获取地图缩放按钮位置
        Log.e(TAG, "settingZoom: " + uiSettings.getZoomPosition());
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

//        double latitude = amapLocation.getLatitude();
//
//        double longitude = amapLocation.getLongitude();
//
//        latLng = new LatLng(latitude, longitude);

//        if (followMove) {
//
//            aMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//
//        }
//
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @OnClick(R.id.iv_location)
    public void onViewClicked() {
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

//    /**
//     * 停止定位
//     */
//    @Override
//    public void deactivate() {
//        mListener = null;
//        if (mlocationClient != null) {
//            mlocationClient.stopLocation();
//            mlocationClient.onDestroy();
//        }
//        mlocationClient = null;
//    }
}
