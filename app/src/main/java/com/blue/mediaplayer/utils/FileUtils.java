package com.blue.mediaplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.text.TextUtils;

import com.blue.mediaplayer.R;
import com.blue.model_basic.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;

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

    public static void saveBitmap(String path, String name, Bitmap bitmap) {
        try {
            File file = new File(path + File.separator + name);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    /**
     * 获取视频文件的缩略图
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    public static Bitmap getVideoThumbnail(Context mContext, String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        try {
            // 获取视频的缩略图
            bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.video_default_icon);
            }
        } catch (Exception e) {
        }
        return bitmap;
    }
}
