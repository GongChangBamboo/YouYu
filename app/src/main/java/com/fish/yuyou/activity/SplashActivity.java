package com.fish.yuyou.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.fish.yuyou.R;
import com.fish.yuyou.base.BaseActivity;
import com.fish.yuyou.util.CommonUtil;
import com.fish.yuyou.util.PermissionUtil;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import org.json.JSONObject;

import java.util.List;

public class SplashActivity extends BaseActivity implements PermissionUtil.CheckPermission {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 不显示系统的标题栏，保证windowBackground和界面activity_main的大小一样，显示在屏幕不会有错位（去掉这一行试试就知道效果了）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        setDefaultInit();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
//        requestPermissionSdcard();
        String[] permission = new String[]{
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION,
                Permission.READ_PHONE_STATE,
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS",
                "android.permission.ACCESS_WIFI_STATE"
        };
        PermissionUtil.requestPermission(mContext, permission, this);
    }

    @Override
    protected void initListener() {

    }

    /**
     * Request permissions.（里面处理登录等操作）
     */
    private void requestPermissionSdcard() {
        String[] permission = new String[]{
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION,
                Permission.READ_PHONE_STATE,
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS",
                "android.permission.ACCESS_WIFI_STATE"
        };
        AndPermission.with(mContext)
                .runtime()
                .permission(permission)
                .onGranted(permissions -> {
                    // Storage permission are allowed.
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            Intent i = new Intent(mContext, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, 1000);
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                    CommonUtil.show(mContext, "打开权限失败");
                    finish();
                    if (AndPermission.hasAlwaysDeniedPermission(mContext, permissions)) {
                        showSettingDialog(mContext, permissions);
                    }
                })
                .start();

    }


    /**
     * Display setting dialog.
     */
    @SuppressLint({"StringFormatInvalid", "LocalSuppress"})
    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);//, TextUtils.join("\n", permissionNames)
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(context.getString(R.string.timdialog))
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.setting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission();
                    }
                })
                .setNegativeButton(context.getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    /**
     * Set permissions.
     */
    private void setPermission() {
        AndPermission.with(this)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        Toast.makeText(mContext, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    @Override
    public void havePermission() {
        Log.e("TAG", "havePermission");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(mContext, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);
    }

}
