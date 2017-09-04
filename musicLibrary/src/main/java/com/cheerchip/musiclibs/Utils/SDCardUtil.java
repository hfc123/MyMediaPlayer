package com.cheerchip.musiclibs.Utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by hfc on 2016/10/21.
 */

public class SDCardUtil {
    public static boolean isLocal(String songid) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyMusic" + File.separator + songid + ".mp3";
            File file = new File(filePath);
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }
    //搜索音乐的方法只能搜索本地音乐

}
