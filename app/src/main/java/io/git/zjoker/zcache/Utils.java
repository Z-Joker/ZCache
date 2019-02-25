package io.git.zjoker.zcache;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;

import java.security.MessageDigest;

public class Utils {

    public static int getVersionCode(Context context) {
        int versionName = 1;
        try {
            PackageManager pm = context.getPackageManager();
            versionName = pm.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
//            Log.e(e);
        }
        return versionName;
    }

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

    public static int parseInt(@Nullable String str) {
        int value = 0;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
        }
        return value;
    }

    public static long parseLong(@Nullable String str) {
        long value = 0;
        try {
            value = Long.parseLong(str);
        } catch (NumberFormatException e) {
        }
        return value;
    }
}
