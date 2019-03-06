package io.git.zjoker.zcache.core;

import java.io.IOException;

import io.git.zjoker.zcache.utils.CacheUtil;
import io.git.zjoker.zcache.utils.DiskLruCacheUtil;
import io.git.zjoker.zcache.converter.IByteConverter;

/**
 * Disk cache Cache supported by DiskLruCache.
 */

public class DiskCacheCore implements ICacheCore {
    private DiskLruCacheUtil diskLruCacheUtil;

    public DiskCacheCore(int appVersion, String cacheDir, int maxSize) {
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
        putWithDeadLine(key, obj, System.currentTimeMillis() + duration, converter);
    }

    @Override
    public <T> void putWithDeadLine(String key, T obj, long deadLine, IByteConverter<T> converter) {
        CacheUtil.validateKey(key);
        diskLruCacheUtil.put(key, CacheUtil.buildByteWithDeadLine(deadLine, converter.obj2Bytes(obj)));
    }

    @Override
    public <T> T get(String key, IByteConverter<T> converter) {
        CacheUtil.validateKey(key);
        byte[] bytes = diskLruCacheUtil.getAsByte(key);
        if (bytes == null) {
            return null;
        }
        if (CacheUtil.isExpired(bytes)) {
            evict(key);
            return null;
        }

        return converter.bytes2Obj(CacheUtil.clearDeadLineInfo(bytes));
    }

    @Override
    public boolean isExpired(String key) {
        CacheUtil.validateKey(key);
        byte[] bytes = diskLruCacheUtil.getAsByte(key);
        return bytes != null && CacheUtil.isExpired(bytes);
    }

    @Override
    public void evict(String key) {
        CacheUtil.validateKey(key);
        diskLruCacheUtil.remove(key);
    }

    @Override
    public void evictAll() {
        diskLruCacheUtil.clear();
    }

    @Override
    public boolean isCached(String key) {
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
