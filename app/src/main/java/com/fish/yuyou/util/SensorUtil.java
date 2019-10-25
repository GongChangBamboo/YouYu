package com.fish.yuyou.util;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;

public class SensorUtil implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor mSensor;
    Sensor aSensor;

//    public void seneorInit(Context context) {
//        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
////        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);陀螺仪
//        mSensorManager.registerListener(myListener, aSensor,
//                SensorManager.SENSOR_DELAY_GAME);
//        mSensorManager.registerListener(myListener, mSensor,
//                SensorManager.SENSOR_DELAY_GAME);
//    }
//
    //当传感器的值发生变化时
    @Override
    public void onSensorChanged(SensorEvent event) {
//        float[] accelerometerValues;
//        float[] magneticFieldValues;
//        float[] floats;
//        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            accelerometerValues = event.values.clone();
//        }
//        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//            magneticFieldValues = event.values.clone();
//        }
//        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//            floats = event.values[1];
//        }
//
//        //方向传感器
//        // 调用getRotaionMatrix获得变换矩阵R[]
//        SensorManager.getRotationMatrix(R2, null, accelerometerValues,
//                magneticFieldValues);
//        SensorManager.getOrientation(R2, values);
//        // 经过SensorManager.getOrientation(R, values);得到的values值为弧度
//        // 转换为角度
//        values[0] = (float) Math.toDegrees(values[0]);
////            values[2] = -values[2];
////            if (values[1] > 0) {
////                values[1] += 180;
////            }
//        if (!isEntering) {
//            setMyMarkerStyleUnEntitingXuanZhuan(values[0]);
//        }
//
//
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//                .fromView(view1));
//        myLocationStyle.anchor(0, 0);
//        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
//        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
//        aMap.setMyLocationStyle(myLocationStyle);
    }

    //当传感器的精度发生变化时
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface ISensorCallBack {
        void seneorCallBack(String str, double lat, double lgt, AMapLocation aMapLocation);
    }

//    public void setSensorCallBack(ISensorCallBack seneorCallBack) {
//        this.sensorCallBack = seneorCallBack;
//    }
}
