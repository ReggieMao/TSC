package com.ebei.library.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MaoLJ on 2018/7/18.
 * 文件工具类
 */

public class FileUtil {

    private static final String TAG = "FileUtil";

    private FileUtil() {

    }

    /**
     * 时间命名
     */
    private static final String TIME_STRING = "yyyyMMdd_HHmmss";

    /**
     * 缓存文件根目录名
     */
    private static final String FILE_DIR = "DCIM";

    /**
     * 上传的照片文件路径
     */
    private static final String UPLOAD_FILE = "Camera";

    /**
     * SD卡是否存在
     */
    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取上传的路径
     */
    public static String getUploadPath(Context context) {
        if (isSDCardExist()) {
            String path = Environment.getExternalStorageDirectory() + File.separator + FILE_DIR + File.separator + UPLOAD_FILE + File.separator;
            File directory = new File(path);
            if (!directory.exists()) directory.mkdirs();
            return path;
        } else {
            File directory = new File(context.getCacheDir(), FILE_DIR + File.separator + UPLOAD_FILE);
            if (!directory.exists()) directory.mkdirs();
            return directory.getAbsolutePath();
        }
    }

    public static String getUploadPath2(Context context, int userId) {
        if (isSDCardExist()) {
            String path = Environment.getExternalStorageDirectory() + File.separator + "AutoChat" + File.separator + "Dynamic" + File.separator + userId + File.separator;
            File directory = new File(path);
            if (!directory.exists()) directory.mkdirs();
            return path;
        } else {
            File directory = new File(context.getCacheDir(), "AutoChat" + File.separator + "Dynamic" + File.separator + userId);
            if (!directory.exists()) directory.mkdirs();
            return directory.getAbsolutePath();
        }
    }

    public static String getUploadPath3(Context context, String date) {
        if (isSDCardExist()) {
            String path = Environment.getExternalStorageDirectory() + File.separator + "AutoChat" + File.separator + "Chat" + File.separator + date + File.separator;
            File directory = new File(path);
            if (!directory.exists()) directory.mkdirs();
            return path;
        } else {
            File directory = new File(context.getCacheDir(), "AutoChat" + File.separator + "Chat" + File.separator + date);
            if (!directory.exists()) directory.mkdirs();
            return directory.getAbsolutePath();
        }
    }

    public static String getTimeString() {
        return new SimpleDateFormat(TIME_STRING).format(new Date());
    }

    /**
     * 保存头像
     */
    public static void saveAvatar(Context context, String path) {
        try {
            URL url = new URL(path);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(getUploadPath(context) + getTimeString() + ".jpg");
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(path));
            intent.setData(uri);
            context.sendBroadcast(intent);
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存动态
     */
    public static void saveDynamic(Context context, String path, String name, int userId) {
        try {
            URL url = new URL(path);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(getUploadPath2(context, userId) + name);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存聊天视频
     */
    public static void saveChatVideo(Context context, String path, String name, String date) {
        try {
            URL url = new URL(path);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(getUploadPath3(context, date) + name);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件夹下所有文件,适当放到子线程中执行
     */
    public static void deleteFiles(File file) {
        if (file == null || !file.exists()) return;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (!f.isDirectory()) {
                    f.delete();
                }
            }
        } else {
            file.delete();
        }
    }

    /**
     * 判断某动态文件是否存在
     */
    public static boolean fileIsExists(Context context, String path, int userId) {
        try {
            File f = new File(getUploadPath2(context, userId) + path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 判断某聊天视频文件是否存在
     */
    public static boolean fileIsExists1(Context context, String path, String date) {
        try {
            File f = new File(getUploadPath3(context, date) + path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
