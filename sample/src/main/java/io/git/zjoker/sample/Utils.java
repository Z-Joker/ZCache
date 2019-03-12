package io.git.zjoker.sample;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.security.MessageDigest;

public class Utils {
    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return "";
        }
    }

    public static File getDiskCacheDir(Context context) {
        final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState()) ? getExternalCacheDir(context)
                .getPath() : context.getCacheDir().getPath();
        return new File(cachePath);
    }

    public static File getExternalCacheDir(Context context) {
        final String cacheDir = "/Android/data/" + context.getPackageName()
                + "/cache/";
        return new File(Environment.getExternalStorageDirectory().getPath()
                + cacheDir);
    }
}
