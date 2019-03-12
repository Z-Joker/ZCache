package io.git.zjoker.zcache.core;

import java.io.IOException;

import io.git.zjoker.zcache.converter.IByteConverter;
import io.git.zjoker.zcache.utils.CacheUtil;
import io.git.zjoker.zcache.utils.DiskLruCacheUtil;
import io.git.zjoker.zcache.utils.LogUtil;

/**
 * Disk cache Cache supported by DiskLruCache.
 */

public class DiskCache implements ICache {
    private DiskLruCacheUtil diskLruCacheUtil;

    public DiskCache(int appVersion, String cacheDir, int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSpace must > 0");
        }
        try {
            diskLruCacheUtil = new DiskLruCacheUtil(appVersion, cacheDir, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void put(String key, T obj, long duration, IByteConverter<T> converter) {
        long deadLine = CacheUtil.validateDuration(duration) ? System.currentTimeMillis() + duration : C_Without_Duration;
        putWithDeadLine(key, obj, deadLine, converter);
    }

    @Override
    public <T> void putWithDeadLine(String key, T obj, long deadLine, IByteConverter<T> converter) {
        CacheUtil.validateKey(key);
        diskLruCacheUtil.put(key, CacheUtil.buildByteWithDeadLine(deadLine, converter.obj2Bytes(obj)));
        LogUtil.d(String.format("Put a cache into disk. Key=%s, Value=%s, DeadLine=%s", key, obj.toString(), deadLine));
    }

    @Override
    public <T> T get(String key, IByteConverter<T> converter) {
        CacheUtil.validateKey(key);
        byte[] bytes = diskLruCacheUtil.getAsByte(key);
        if (bytes == null) {
            return null;
        }
        if (CacheUtil.isExpired(bytes)) {
            LogUtil.d(String.format("Cache in disk of the key named %s expired", key));
            remove(key);
            return null;
        }

        LogUtil.d(String.format("Get a Cache from disk of the key named %s.", key));
        return converter.bytes2Obj(CacheUtil.clearDeadLineInfo(bytes));
    }

    @Override
    public boolean isExpired(String key) {
        CacheUtil.validateKey(key);
        byte[] bytes = diskLruCacheUtil.getAsByte(key);
        return bytes != null && CacheUtil.isExpired(bytes);
    }

    @Override
    public void remove(String key) {
        CacheUtil.validateKey(key);
        diskLruCacheUtil.remove(key);
    }

    @Override
    public void removeAll() {
        diskLruCacheUtil.clear();
    }

    @Override
    public boolean contains(String key) {
        CacheUtil.validateKey(key);
        return diskLruCacheUtil.contains(key);
    }

    @Override
    public long getDeadLine(String key) {
        CacheUtil.validateKey(key);
        byte[] bytes = diskLruCacheUtil.getAsByte(key);
        if (bytes == null) {
            return 0;
        }
        return CacheUtil.parseLong(CacheUtil.getDeadLine(bytes));
    }
}
