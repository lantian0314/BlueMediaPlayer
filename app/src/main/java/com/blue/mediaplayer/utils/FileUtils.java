package com.blue.mediaplayer.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by xingyatong on 2018/6/12 13:31
 * Describe File助手类
 */
public class FileUtils {

    public static String getExtension(String name) {
        String ext;

        if (name.lastIndexOf(".") == -1) {
            ext = "";

        } else {
            int index = name.lastIndexOf(".");
            ext = name.substring(index + 1, name.length());
        }
        return ext;
    }

    /**
     * 获取全路径中的文件名
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }

    public static String getSdcardRootPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static File[] getAllFile(String path) {
        File file = new File(path);
        File[] files = null;
        if (file != null) {
            files = file.listFiles();
        }
        return files;
    }
}
