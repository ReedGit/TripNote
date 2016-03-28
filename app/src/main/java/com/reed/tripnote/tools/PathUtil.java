package com.reed.tripnote.tools;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;

public class PathUtil {

    private static boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();//获取外部存储设备的当前状态
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * 如果挂载外置存储，则缓存路径在外置存储上，否则缓存路径在内置存储上
     */
    public static File getAvailableCacheDir(Context context) {
        if (isExternalStorageWritable()) {
            return context.getExternalCacheDir();
        } else {
            // 只有此应用才能访问。拍照的时候有问题，因为拍照的应用写入不了该文件
            return context.getCacheDir();
        }
    }

    //拍照保存的地址
    public static String getTakePhotoPath(Context context){
        return new File(getAvailableCacheDir(context),"picture_"+System.currentTimeMillis()).getAbsolutePath();
    }

    //获取缓存大小
    public static long getFileSize(File f)// 取得文件夹大小
    {
        long size = 0;
        File files[] = f.listFiles();
        for (File file: files) {
            if (file.isDirectory()) {
                size += getFileSize(file);
            } else {
                size += file.length();
            }
        }
        return size;
    }

    public static String formatFileSize(long size) {// 转换文件大小
        if(size == 0){
            return "0KB";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSize;
        if (size < 1024) {
            fileSize = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSize = df.format((double) size / 1024) + "K";
        } else if (size < 1073741824) {
            fileSize = df.format((double) size / 1048576) + "M";
        } else {
            fileSize = df.format((double) size / 1073741824) + "G";
        }
        return fileSize;
    }

}
