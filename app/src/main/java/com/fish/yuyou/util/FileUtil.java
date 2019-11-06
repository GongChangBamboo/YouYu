package com.fish.yuyou.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.MEDIA_MOUNTED;

public class FileUtil {

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    //获取存储sd卡根目录
    public static String getRootFilePath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + File.separator + "data"; // filePath:
        }
    }

    //这个是外部存储的私有目录 在sdcard/Android/data/包名/files/..里面dir文件夹的这个路径
    public static String getExternalFilePath(Context context, String dir) {
        String directoryPath = "";
        //判断SD卡是否可用
        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            directoryPath = context.getExternalFilesDir(dir).getAbsolutePath();
        } else {
            directoryPath = context.getFilesDir() + File.separator + dir;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }

    /**
     * 功能:初始化目录
     */
    private static void initDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getDownloadDir() {
        String path = Environment.getExternalStorageDirectory() + File.separator + PackageInfoUtil.getPackageName();
        initDir(path);
        return path;
    }

    public static String getLocationText() {
        String path = getDownloadDir() + File.separator + "locat";
        initDir(path);
        return path;
    }

    /**
     * 写入TXT文件内容
     *
     * @param filename   文件完整路径
     * @param contentstr 保存的内容
     */
    public static void saveTxtFile(String filename, String contentstr) {
        File file = new File(filename);
        try {
            FileWriter out = new FileWriter(file);
            out.write(contentstr);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 使用BufferedWriter进行文本内容的追加
     *
     * @param file
     * @param content
     */
    public static void addTxtToFileBuffered(File file, String content) {
        //在文本文本中追加内容
        BufferedWriter out = null;
        try {
            //FileOutputStream(file, true),第二个参数为true是追加内容，false是覆盖
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.newLine();//换行
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取TXT文件内容
     *
     * @param filename
     * @return TXT内容
     */
    public static List<String> loadTxtFile(String filename) {
        List<String> newList = new ArrayList<>();
        File file = new File(filename);
        try {
            InputStream instream = new FileInputStream(file);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    newList.add(line);
                }
                instream.close();
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        return newList;
    }
}
