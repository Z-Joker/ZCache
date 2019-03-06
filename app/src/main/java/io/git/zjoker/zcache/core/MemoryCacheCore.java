package io.git.zjoker.zcache.core;

import android.util.LruCache;

import java.io.Serializable;

import io.git.zjoker.zcache.CacheUtils;
import io.git.zjoker.zcache.mapper.IByteConverter;

import static io.git.zjoker.zcache.helper.ICacheHelper.C_Illegal_Duration;
/**
 * Memory Cache used LruCache.
 * */

public class MemoryCacheCore implements ICacheCore {
    private LruCache<String, CacheEntry> cacheMap;

    public MemoryCacheCore(int maxSize) {
        cacheMap = new LruCache<String, CacheEntry>(maxSize) {
            @Override
            protected int sizeOf(String key, CacheEntry value) {
                return value.bytes.length;
            }
        };
    }

    @Override
    public <T> void put(String key, T obj, IByteConverter<T> converter) {
        put(key, obj, C_Illegal_Duration, converter);
    }

    @Override
    public <T> void put(String key, T obj, long duration, IByteConverter<T> converter) {
        cacheMap.put(key, new CacheEntry(converter.getBytes(obj), CacheUtils.fixDuration(duration)));
    }

    public <T> void put(String key, T obj, long saveTime, long duration, IByteConverter<T> mapper) {
        cacheMap.put(key, new CacheEntry(mapper.getBytes(obj), saveTime, CacheUtils.fixDuration(duration)));
    }

    @Override
    public <T> T get(String key, IByteConverter<T> converter) {
        CacheEntry cacheEntry = cacheMap.get(key);
        if (cacheEntry == null) {
            return null;
        }
        if (CacheUtils.isExpired(cacheEntry.storageTime, cacheEntry.duration)) {
            evict(key);
            return null;
        }
        return converter.getObject(cacheEntry.bytes);
    }

    @Override
    public boolean isExpired(String key) {
        CacheEntry cacheEntry = cacheMap.get(key);
        return cacheEntry != null && CacheUtils.isExpired(cacheEntry.storageTime, cacheEntry.duration);
    }

    @Override
    public void evict(String key) {
        if (cacheMap.get(key) != null) {
            cacheMap.remove(key);
        }
    }

    @Override
    public void evictAll() {
        cacheMap.evictAll();
    }

    @Override
    public boolean isCached(String key) {
        return cacheMap.get(key) != null;
    }

    @Override
    public long[] getDurationInfo(String key) {
        CacheEntry cacheEntry = cacheMap.get(key);
        if (cacheEntry == null) {
            return null;
        }
        return new long[]{cacheEntry.storageTime, cacheEntry.duration};
    }

    private static class CacheEntry implements Serializable {
        byte[] bytes;
        long storageTime;
        long duration;

        public CacheEntry(byte[] bytes, long duration) {
            this.bytes = bytes;
            this.storageTime = System.currentTimeMillis();
            this.duration = duration;
        }

        public CacheEntry(byte[] bytes, long storageTime, long duration) {
            this.bytes = bytes;
            this.storageTime = storageTime;
            this.duration = duration;
        }
    }
}
