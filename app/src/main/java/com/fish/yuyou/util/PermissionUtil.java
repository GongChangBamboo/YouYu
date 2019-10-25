package com.fish.yuyou.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.fish.yuyou.R;
import com.fish.yuyou.activity.MainActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import java.util.List;

public class PermissionUtil {
    /**
     * Request permissions.（里面处理登录等操作）
     */
    public static void requestPermission(Context mContext, String[] permission, CheckPermission checkPermission) {
        AndPermission.with(mContext)
                .runtime()
                .permission(permission)
                .onGranted(permissions -> {
                    // Storage permission are allowed.
                    checkPermission.havePermission();
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                    CommonUtil.show(mContext, "打开权限失败");
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
    private static void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);//, TextUtils.join("\n", permissionNames)
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(context.getString(R.string.timdialog))
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.setting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission(context);
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
    private static void setPermission(Context mContext) {
        AndPermission.with(mContext)
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

    public interface CheckPermission {
        void havePermission();

    }
}
