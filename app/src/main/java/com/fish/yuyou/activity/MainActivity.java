package com.fish.yuyou.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.fish.yuyou.R;
import com.fish.yuyou.base.BaseActivity;
import com.fish.yuyou.util.BitmapUtil;
import com.fish.yuyou.util.LocationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements LocationSource {
    private final static String TAG = "MainActivity";

    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    private AMap aMap;
    //定位监听器
    private LocationSource.OnLocationChangedListener mLocationListener = null;
    private LocationUtil locationUtil;
    private UiSettings uiSettings;
    private LatLng latLng;
    //传感器管理器
    private SensorManager sensorManager;
    private Sensor mGyroscope;

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
        //加载地图
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        latLng = new LatLng(40, 116);
        uiSettings = aMap.getUiSettings();
        settingZoom();
        //是否显示指南针
        uiSettings.setCompassEnabled(true);
        //开启比例尺
        uiSettings.setScaleControlsEnabled(true);
        //  设置logo位置
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        //显示默认的定位按钮
        uiSettings.setMyLocationButtonEnabled(true);
        // 可触发定位并显示当前位置
        aMap.setMyLocationEnabled(true);
        //这里设置定位为了在点击定位按钮后，显示地图定位的位置方便查看
        //注意这里有个坑，在点击定位后发现定位到了默认的位置（海里面），造成这种情况并不是权限和代码的问题，
        //遇到这种情况时，需要手动将GPS定位打开就OK了
        MyLocationStyle mls = new MyLocationStyle();
        mls.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        //修改定位标志样式
        mls.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.readBitmapById(mContext, R.mipmap.location_navi)));
//        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationStyle(mls);
        setGestures();
        setLocationCallBack();
        setSensor();
    }

    private void setSensor() {
        //获得系统传感器服务管理权
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        /*
        加速传感器    　　 Sensor.TYPE_ACCELEROMETER
        陀螺仪传感器  　   Sensor.TYPE_GYROSCOPE
        环境光仪传感器     Sensor.TYPE_LIGHT
        电磁场传感器    　 Sensor.TYPE_MAGNETIC_FIELD
        方向传感器    　　 Sensor.TYPE_ORIENTATION:
        压力传感器    　　 Sensor.TYPE_PRESSURE:
        距离传感器   　　  Sensor.TYPE_PROXIMITY:
        温度传感器   　　  Sensor.TYPE_TEMPERATURE:
        */
        mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(sensorEventListener, mGyroscope,
                SensorManager.SENSOR_DELAY_UI);
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
        uiSettings.setAllGesturesEnabled(true);
        //指定手势中心点
//        aMap.setPointToCenter(100,100);

        //是否开启中心为手势中心,是否可以旋转地图
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

    private void setLocationCallBack() {
        locationUtil = new LocationUtil();
        locationUtil.setLocationCallBack(new LocationUtil.ILocationCallBack() {
            @Override
            public void locationCallBack(String str, double lat, double lgt, AMapLocation aMapLocation) {
                latLng = new LatLng(lat, lgt);
//                //根据获取的经纬度，将地图移动到定位位置
//                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lgt)));
                mLocationListener.onLocationChanged(aMapLocation);
//                //添加定位图标
//                aMap.addMarker(locationUtil.getMarkerOption(str, lat, lgt));
            }
        });
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //传感器值改变
//            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//                float floats = event.values[1];
//
//            }
//            //方向传感器
//            // 调用getRotaionMatrix获得变换矩阵R[]
//            SensorManager.getRotationMatrix(R2, null, accelerometerValues,
//                    magneticFieldValues);
//            SensorManager.getOrientation(R2, values);
//            // 经过SensorManager.getOrientation(R, values);得到的values值为弧度
//            // 转换为角度
//            values[0] = (float) Math.toDegrees(values[0]);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //传感器精确度改变
        }
    };

    @Override
    protected void initListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
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

    @OnClick(R.id.iv_location)
    public void onViewClicked() {
        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 10, 0, 0));
        aMap.moveCamera(mCameraUpdate);
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationListener = onLocationChangedListener;
        locationUtil.startLocate(getApplicationContext());
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mLocationListener = null;
    }

}
