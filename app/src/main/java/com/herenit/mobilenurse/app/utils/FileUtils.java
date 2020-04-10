package com.herenit.mobilenurse.app.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.herenit.mobilenurse.app.MobileNurseApplication;
import com.herenit.mobilenurse.criteria.constant.CommonConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * author: HouBin
 * date: 2019/1/11 15:16
 * desc: 文件工具类
 */
public class FileUtils {
    private static final String TAG = "FileTag";
    //App下载存储路径
    public static final String APP_EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory() + "/MobileNurse/";
    //床位一览本地定义的查询条件
    public static final String FILE_NAME_UI_CONDITIONS_SICKBED = "ui_condition_sickbed.json";
    public static final String FILE_NAME_UI_CONDITIONS_ORDER = "ui_condition_order.json";
    //常用时间范围条件
    public static final String FILE_NAME_UI_CONDITION_COMMON_TIME_INTERVAL = "ui_condition_common_time_interval.json";
    public static final String FILE_NAME_UI_OPERATION_TIME_INTERVAL = "ui_operation_time_interval.json";

    private FileUtils() {
    }

    /**
     * 获取app下载文件存储目录
     *
     * @return
     */
    public static File getAppDownloadSaveDirectory() {
        String path = APP_EXTERNAL_STORAGE_PATH + "download";
        File downloadPath = new File(path);
        if (!downloadPath.exists()) {
            downloadPath.mkdirs();
        }
        return downloadPath;
    }

    /**
     * 将输入流写入指定文件中
     *
     * @param inputStream
     * @param file
     */
    public static void writeStreamToFile(InputStream inputStream, File file) {
        if (file == null || inputStream == null)
            return;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = inputStream.read(buf)) != -1) {
                fos.write(buf, 0, count);
                fos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 读取assets里的json文件,转成String
     *
     * @param fileName
     * @return
     */
    public static String getAssetsToString(String fileName) {
        Context context = MobileNurseApplication.getInstance().getApplicationContext();
        StringBuilder sb = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
            br.close();
            Log.i(TAG, "getJson from :" + fileName + " success");
        } catch (IOException e) {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String createCommonFileName(String fileType) {
        String fileName = "";
        if (TextUtils.isEmpty(fileType))
            return fileName;
        if (CommonConstant.FILE_TYPE_NAME_PDF.endsWith(fileType)) {//PDF文件
            fileName = "common.pdf";
        }
        return fileName;
    }

    public static String getCommonFileNameUrl(String fileType) {
        String fileName = createCommonFileName(fileType);
        File file = new File(getAppDownloadSaveDirectory(), fileName);
        if (file.isFile() && file.exists())
            return file.getPath();
        return null;
    }
}
