package com.fish.yuyou.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Math.PI;

public class MainActivity extends BaseActivity implements LocationSource {
    private final static String TAG = "MainActivity";

    @BindView(R.id.mapview)
    MapView mapview;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.tv_angle_fuyang)
    TextView tvAngleFuyang;
    @BindView(R.id.tv_angle_henggun)
    TextView tvAngleHenggun;
    @BindView(R.id.tv_angle_orientation)
    TextView tvAngleOrientation;
    private AMap aMap;
    //定位监听器
    private OnLocationChangedListener mLocationListener = null;
    private LocationUtil locationUtil;
    private UiSettings uiSettings;
    private LatLng latLng;
    //传感器管理器
    private SensorManager sensorManager;
    private long timestamp;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float angle0;
    private float angle1;
    private float angle2;
    //用户当前位置参数
    private float rotateAngle0;
    private float rotateAngle1;
    private float rotateAngle2;
    private float[] xDir = new float[4];
    private float[] yDir = new float[4];
    private float[] zDir = new float[4];
    private float B;
    private float theta;
    private double alpha;
    private Timer timer;
    private int axis_x;
    private int axis_y;
    private Bitmap locBitmap;//定位标志图标
    private float rotateAngle = 0;//定位标志当前旋转的角度（方位角，与北极夹角，顺时针为正）
    MyLocationStyle mls;


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
        uiSettings = aMap.getUiSettings();
        settingZoom();
        //是否显示指南针
        uiSettings.setCompassEnabled(false);
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
        mls = new MyLocationStyle();
        mls.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        //修改定位标志样式
        locBitmap = BitmapUtil.readBitmapById(mContext, R.mipmap.location_navi);
        locBitmap = BitmapUtil.rotaingImageView(-45, locBitmap);
        mls.myLocationIcon(BitmapDescriptorFactory.fromBitmap(locBitmap));
        aMap.setMyLocationStyle(mls);
        setGestures();
        setLocationCallBack();
        //获得系统传感器服务管理权
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        initSensor();
        timer = new Timer("MyTimer");
        timer.schedule(new MyTask(), 0, 1000);
    }

    private void initSensor() {
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
        // 陀螺仪触感器
        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // 地磁传感器
        Sensor magneticSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // 加速度传感器
        Sensor accelerometerSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // 为传感器注册监听
        sensorManager.registerListener(sensorEventListener, magneticSensor,
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(sensorEventListener, accelerometerSensor,
                SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(sensorEventListener, gyroSensor,
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
//        uiSettings.setAllGesturesEnabled(true);
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
        locationUtil = new LocationUtil(2000);
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
        float[] accelerometerValues = new float[3];
        float[] magneticValues = new float[3];
        float[] ayroValues = new float[3];

        @Override
        public void onSensorChanged(SensorEvent event) {
            //传感器值改变
            // 判断当前是加速度传感器还是地磁传感器
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                // 注意赋值时要调用clone()方法
                magneticValues = event.values.clone();
                Log.e("TAG", "sensor=地磁传感器");
            } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // 注意赋值时要调用clone()方法
                accelerometerValues = event.values.clone();
                Log.e("TAG", "sensor=加速度传感器");
            } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                ayroValues = event.values.clone();
                Log.e("TAG", "sensor=陀螺仪传感器");
//                if (timestamp != 0) {
//                    final float dT = (event.timestamp - timestamp) * NS2S;
//                    angle0 += ayroValues[0] * dT;
//                    angle1 += ayroValues[1] * dT;
//                    angle2 += ayroValues[2] * dT;
//                }
//                timestamp = event.timestamp;
            }
            float[] R = new float[9];
            float[] values = new float[3];

            SensorManager.getRotationMatrix(R, null, accelerometerValues,
                    magneticValues);
            SensorManager.getOrientation(R, values);

            rotateAngle0 = (float) Math.toDegrees(values[0]);//azimuth, rotation around the Z axis.
            rotateAngle1 = (float) Math.toDegrees(values[1]);//pitch, rotation around the X axis.
            rotateAngle2 = (float) Math.toDegrees(values[2]);//roll, rotation around the Y axis.

//            float[] rollMat = new float[16];
//            float roll = rotateAngle2;
//            Matrix.setRotateM(rollMat, 0, roll, 0, 1, 0);
//
//            float[] pitchMat = new float[16];
//            float pitch = -rotateAngle1;
//            Matrix.setRotateM(pitchMat, 0, pitch, 1, 0, 0);
//
//            float[] yawMat = new float[16];
//            float yaw = -rotateAngle0;
//            Matrix.setRotateM(yawMat, 0, yaw, 0, 0, 1);
//
//            float[] tmpMat = new float[16];
//            float[] rotateMat = new float[16];
//            Matrix.multiplyMM(tmpMat, 0, pitchMat, 0, rollMat, 0);
//            Matrix.multiplyMM(rotateMat, 0, yawMat, 0, tmpMat, 0);
//
//            float[] srcVec = new float[4];
//            float[] destVec = new float[4];
//
//            srcVec[0] = 1;
//            srcVec[1] = 0;
//            srcVec[2] = 0;
//            srcVec[3] = 1;
//            Matrix.multiplyMV(destVec, 0, rotateMat, 0, srcVec, 0);
//            xDir[0] = destVec[0];
//            xDir[1] = destVec[1];
//            xDir[2] = destVec[2];
//            xDir[3] = destVec[3];
//
//            srcVec[0] = 0;
//            srcVec[1] = 1;
//            srcVec[2] = 0;
//            srcVec[3] = 1;
//            Matrix.multiplyMV(destVec, 0, rotateMat, 0, srcVec, 0);
//            yDir[0] = destVec[0];
//            yDir[1] = destVec[1];
//            yDir[2] = destVec[2];
//            yDir[3] = destVec[3];
//
//            srcVec[0] = 0;
//            srcVec[1] = 0;
//            srcVec[2] = 1;
//            srcVec[3] = 1;
//            Matrix.multiplyMV(destVec, 0, rotateMat, 0, srcVec, 0);
//            zDir[0] = destVec[0];
//            zDir[1] = destVec[1];
//            zDir[2] = destVec[2];
//            zDir[3] = destVec[3];
            theta = Math.abs(values[0]);
            if (theta > (PI / 2)) {
                theta = (float) (PI - theta);
            }
            B = Math.abs(values[1]);


            alpha = Math.toDegrees(Math.atan(Math.tan(B) / (Math.cos(theta))));
            if (alpha < 0) {
                alpha = -(90 + alpha);
            } else {
                alpha = 90 - alpha;
            }
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
        initSensor();
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

    class MyTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    String msg = String.format("\r\nX:(%f, %f, %f)\r\nY:(%f, %f, %f)\r\nZ:(%f, %f, %f)\r\n",
//                            xDir[0], xDir[1], xDir[2],
//                            yDir[0], yDir[1], yDir[2],
//                            zDir[0], zDir[1], zDir[2]);
//
//                    float xdirHSAngle = (float) Math.asin(Math.max(-1, Math.min(1, xDir[2])));
//
//                    String msg2 = String.format("\r\nxlen:%f, ylen:%f, zlen:%f, xdirHSAngle:%f\r\n",
//                            Math.sqrt(xDir[0] * xDir[0] + xDir[1] * xDir[1] + xDir[2] * xDir[2]),
//                            Math.sqrt(yDir[0] * yDir[0] + yDir[1] * yDir[1] + yDir[2] * yDir[2]),
//                            Math.sqrt(zDir[0] * zDir[0] + zDir[1] * zDir[1] + zDir[2] * zDir[2]),
//                            xdirHSAngle * 180 / Math.PI);
                    tvAngleFuyang.setText("俯仰角:" + rotateAngle1);
//                    fuyang = 90 - Math.toDegrees(B) + "";
                    tvAngleHenggun.setText("横滚角:" + rotateAngle2);
//                    henggun = alpha + "";
                    tvAngleOrientation.setText("方位角:" + rotateAngle0);
                    if (Math.abs(rotateAngle - rotateAngle0) > 10) {
                        rotateAngle = rotateAngle0;
                        mls.myLocationIcon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.rotaingImageView(rotateAngle, locBitmap)));
                        aMap.setMyLocationStyle(mls);
                    }

//                    fangwei = rotateAngle0 + "";

//                    tvAngleFuyang.setText("俯仰角:" + Math.toDegrees(angle0));
//                    tvAngleHenggun.setText("横滚角:" + Math.toDegrees(angle1));
//                    tvAngleOrientation.setText("方位角:" + (90 - Math.toDegrees(angle2)));
                }
            });
        }
    }

//    //获取所在象限
//    public void updateCoordinate() {
//        WindowManager windowManager = ((Activity) mContext).getWindowManager();
//        Display display = windowManager.getDefaultDisplay();
//        int screenRotation = display.getRotation();
//        switch (screenRotation) {
//            case Surface.ROTATION_0:
//                axis_x = SensorManager.AXIS_X;
//                axis_y = SensorManager.AXIS_Y;
//                break;
//            case Surface.ROTATION_90:
//                axis_x = SensorManager.AXIS_X;
//                axis_y = SensorManager.AXIS_Y;
//                break;
//            case Surface.ROTATION_180:
//                break;
//            case Surface.ROTATION_270:
//                break;
//        }
//    }
}
