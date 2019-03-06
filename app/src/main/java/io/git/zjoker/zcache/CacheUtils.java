package io.git.zjoker.zcache;

import static io.git.zjoker.zcache.helper.ICacheHelper.C_Illegal_Duration;

public class CacheUtils {
    /**
     * 判断缓存的byte数据是否到期
     *
     * @param data
     * @return true：到期了 false：还没有到期
     */
    public static boolean isDue(byte[] data) {
        String[] strs = getDateInfoFromDate(data);
        if (strs != null && strs.length == 2) {
            String saveTimeStr = strs[0];
            while (saveTimeStr.startsWith("0")) {
                saveTimeStr = saveTimeStr
                        .substring(1, saveTimeStr.length());
            }
            return isExpired(Long.valueOf(saveTimeStr), Long.valueOf(strs[1]));
        }
        return false;
    }

    public static boolean isExpired(long saveTime, long duration) {
        return System.currentTimeMillis() > saveTime + duration;
    }

    public static byte[] newByteArrayWithDateInfo(long second, byte[] data2) {
        byte[] data1 = createDateInfo(second).getBytes();
        byte[] retdata = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, retdata, 0, data1.length);
        System.arraycopy(data2, 0, retdata, data1.length, data2.length);
        return retdata;
    }

    public static byte[] clearDateInfo(byte[] data) {
        if (hasDateInfo(data)) {
            return copyOfRange(data, indexOf(data, mSeparator) + 1,
                    data.length);
        }
        return data;
    }

    private static boolean hasDateInfo(byte[] data) {
        return data != null && data.length > 15 && data[13] == '-'
                && indexOf(data, mSeparator) > 14;
    }

    public static String[] getDateInfoFromDate(byte[] data) {
        if (hasDateInfo(data)) {
            String saveDate = new String(copyOfRange(data, 0, 13));
            String deleteAfter = new String(copyOfRange(data, 14,
                    indexOf(data, mSeparator)));
            return new String[]{saveDate, deleteAfter};
        }
        return null;
    }

    private static int indexOf(byte[] data, char c) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Fix duration.
     * Return C_Illegal_Duration when duration is Illegal.
     */
    public static long fixDuration(long duration) {
        return isLegalDuration(duration) ? duration : C_Illegal_Duration;
    }

    public static boolean isLegalDuration(long duration) {
        return duration != C_Illegal_Duration && duration > 0;
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

    private static final char mSeparator = ' ';

    private static String createDateInfo(long second) {
        String currentTime = System.currentTimeMillis() + "";
        while (currentTime.length() < 13) {
            currentTime = "0" + currentTime;
        }
        return currentTime + "-" + second + mSeparator;
    }
}
