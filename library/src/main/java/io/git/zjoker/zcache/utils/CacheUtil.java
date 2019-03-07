package io.git.zjoker.zcache.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CacheUtil {
    private static final char mSeparator = '-';
    private static final Pattern LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,64}");

    /**
     * Check the data is expired
     */
    public static boolean isExpired(byte[] data) {
        String saveTimeStr = getDeadLine(data);
        while (saveTimeStr.startsWith("0")) {
            saveTimeStr = saveTimeStr.substring(1);
        }
        return isExpired(parseLong(saveTimeStr));
    }

    /**
     * Check the data is expired
     */
    public static boolean isExpired(long deadline) {
        return System.currentTimeMillis() > deadline;
    }

    public static byte[] buildByteWithDeadLine(long deadLine, byte[] originalData) {
        byte[] deadLineBytes = createDeadLineStr(deadLine).getBytes();
        byte[] dataWithDeadLine = new byte[deadLineBytes.length + originalData.length];
        System.arraycopy(deadLineBytes, 0, dataWithDeadLine, 0, deadLineBytes.length);
        System.arraycopy(originalData, 0, dataWithDeadLine, deadLineBytes.length, originalData.length);
        return dataWithDeadLine;
    }

    public static byte[] clearDeadLineInfo(byte[] data) {
        return copyOfRange(data, indexOf(data, mSeparator) + 1,
                data.length);
    }

    public static String getDeadLine(byte[] data) {
        return new String(copyOfRange(data, 0, indexOf(data, mSeparator)));
    }

    private static int indexOf(byte[] data, char c) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == c) {
                return i;
            }
        }
        return -1;
    }

    private static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }


    private static String createDeadLineStr(long deadLine) {
        return String.valueOf(deadLine) + mSeparator;
    }

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

    public static long parseLong(@Nullable String str) {
        if (str == null) {
            return 0;
        }
        long value = 0;
        try {
            value = Long.parseLong(str);
        } catch (NumberFormatException e) {
        }
        return value;
    }

    public static void close(Closeable... closeables) {
        if (closeables == null || closeables.length == 0) {
            return;
        }
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) != -1) {
                bos.write(buffer, 0, count);
            }
            return bos.toByteArray();
        } finally {
            close(bos);
        }
    }

    public static void validateKey(String key) {
        Matcher matcher = LEGAL_KEY_PATTERN.matcher(key);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,64}: \"" + key + "\"");
        }
    }
}
