package com.fish.yuyou.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * 包信息管理类
 *
 * @author WGL7569
 */
public class PackageInfoUtil {

    //可以直接拿到管理器 和 信息 （已经初始化好了）
    private static PackageManager mPackageManager;
    private static PackageInfo mPackageInfo;

    public static void init(Context context) {
        mPackageManager = context.getPackageManager();
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode() {
        int versionCode = 0;
        if (mPackageInfo != null) {
            versionCode = mPackageInfo.versionCode;
        }
        return versionCode;
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public static String getVersionName() {
        String versionName = "unknownversionname";
        if (mPackageInfo != null) {
            versionName = mPackageInfo.versionName;
        }
        return versionName;
    }

    /*
     * 获取包名
     * */
    public static String getPackageName() {
        String packageName = "unknownpackagename";
        if (mPackageInfo != null) {
            packageName = mPackageInfo.packageName;
        }
        return packageName;
    }

    public static String getSharedUserId() {
        String sharedUserId = "unknownshareduserid";
        if (mPackageInfo != null) {
            sharedUserId = mPackageInfo.sharedUserId;
        }
        return sharedUserId;
    }

    public static String getSignature() {
        String signature = "unknownsignature";
        List<PackageInfo> apps = mPackageManager.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        while (iter.hasNext()) {
            PackageInfo packageinfo = iter.next();
            String packageName = packageinfo.packageName;
            if (packageName.equals(getPackageName())) {
                if (packageinfo.signatures != null) {
                    signature = packageinfo.signatures[0].toCharsString();
                }
            }
        }
        return signature;
    }

    public static String getUninstallAPKSignatures(String apkPath) {
        String PATH_PackageParser = "android.content.pm.PackageParser";
        String sign = "unkown sign";
        try {
            // apk包的文件路径
            // 这是一个Package 解释器, 是隐藏的
            // 构造函数的参数只有一个, apk文件的路径
            // PackageParser packageParser = new PackageParser(apkPath);
            Class pkgParserCls = Class.forName(PATH_PackageParser);
            Class[] typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            Object pkgParser = pkgParserCt.newInstance(valueArgs);
            // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            // PackageParser.Package mPkgInfo = packageParser.parsePackage(new
            // File(apkPath), apkPath,
            // metrics, 0);
            typeArgs = new Class[4];
            typeArgs[0] = File.class;
            typeArgs[1] = String.class;
            typeArgs[2] = DisplayMetrics.class;
            typeArgs[3] = Integer.TYPE;
            Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage", typeArgs);
            valueArgs = new Object[4];
            valueArgs[0] = new File(apkPath);
            valueArgs[1] = apkPath;
            valueArgs[2] = metrics;
            valueArgs[3] = PackageManager.GET_SIGNATURES;
            Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);

            typeArgs = new Class[2];
            typeArgs[0] = pkgParserPkg.getClass();
            typeArgs[1] = Integer.TYPE;
            Method pkgParser_collectCertificatesMtd = pkgParserCls.getDeclaredMethod("collectCertificates", typeArgs);
            valueArgs = new Object[2];
            valueArgs[0] = pkgParserPkg;
            valueArgs[1] = PackageManager.GET_SIGNATURES;
            pkgParser_collectCertificatesMtd.invoke(pkgParser, valueArgs);
            // 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
            Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField("mSignatures");

            Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);
            if (info != null) {
                sign = info[0].toCharsString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    //获取设备的信息 android-6.0.1-HUAWEI-KIW-UL00
    public static String getPlatform() {

        return String.format("android-%s-%s-%s", Build.VERSION.RELEASE, Build.MANUFACTURER, Build.MODEL);
    }
}
