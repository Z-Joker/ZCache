package io.git.zjoker.zcache.core;

import android.util.LruCache;

import java.io.Serializable;

import io.git.zjoker.zcache.converter.IByteConverter;
import io.git.zjoker.zcache.utils.CacheUtil;
import io.git.zjoker.zcache.utils.LogUtil;

/**
 * Memory Cache supported by LruCache.
 */

public class MemoryCache implements ICache {
    private LruCache<String, CacheEntry> cacheMap;

    public MemoryCache(int maxSize) {
        cacheMap = new LruCache<String, CacheEntry>(maxSize) {
            @Override
            protected int sizeOf(String key, CacheEntry value) {
                return value.bytes.length;
            }
        };
    }

    @Override
    public <T> void put(String key, T obj, long duration, IByteConverter<T> converter) {
        putWithDeadLine(key, obj, System.currentTimeMillis() + duration, converter);
    }

    @Override
    public <T> void putWithDeadLine(String key, T obj, long deadLine, IByteConverter<T> converter) {
        CacheUtil.validateKey(key);
        cacheMap.put(key, new CacheEntry(converter.obj2Bytes(obj), deadLine));
        LogUtil.d(String.format("Put a cache into memory. Key=%s, Value=%s, DeadLine=%s", key, obj.toString(), deadLine));
    }

    @Override
    public <T> T get(String key, IByteConverter<T> converter) {
        CacheUtil.validateKey(key);
        CacheEntry cacheEntry = cacheMap.get(key);
        if (cacheEntry == null) {
            return null;
        }
        if (CacheUtil.isExpired(cacheEntry.deadLine)) {
            LogUtil.d(String.format("Cache in memory of the key named %s expired", key));
            remove(key);
            return null;
        }
        LogUtil.d(String.format("Get a Cache from memory of the key named %s.", key));
        return converter.bytes2Obj(cacheEntry.bytes);
    }

    @Override
    public boolean isExpired(String key) {
        CacheUtil.validateKey(key);
        CacheEntry cacheEntry = cacheMap.get(key);
        return cacheEntry != null && CacheUtil.isExpired(cacheEntry.deadLine);
    }

    @Override
    public void remove(String key) {
        CacheUtil.validateKey(key);
        cacheMap.remove(key);
    }

    @Override
    public void removeAll() {
        cacheMap.evictAll();
    }

    @Override
    public boolean contains(String key) {
        CacheUtil.validateKey(key);
        return cacheMap.get(key) != null;
    }

    @Override
    public long getDeadLine(String key) {
        CacheUtil.validateKey(key);
        CacheEntry cacheEntry = cacheMap.get(key);
        if (cacheEntry == null) {
            return 0;
        }
        return cacheEntry.deadLine;
    }

    private static class CacheEntry implements Serializable {
        byte[] bytes;
        long deadLine;

        public CacheEntry(byte[] bytes, long deadLine) {
            this.bytes = bytes;
            this.deadLine = deadLine;
        }
    }
}
