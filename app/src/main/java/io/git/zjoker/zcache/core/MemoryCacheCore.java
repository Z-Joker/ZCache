package io.git.zjoker.zcache.core;

import android.util.LruCache;

import java.io.Serializable;

import io.git.zjoker.zcache.CacheUtils;
import io.git.zjoker.zcache.mapper.IByteMapper;

import static io.git.zjoker.zcache.helper.ICacheHelper.C_Illegal_Duration;

public class MemoryCacheCore implements ICacheCore {
    private LruCache<String, CacheEntry> bytesMap;

    public MemoryCacheCore(int maxSize) {
        bytesMap = new LruCache<String, CacheEntry>(maxSize) {
            @Override
            protected int sizeOf(String key, CacheEntry value) {
                return value.bytes.length;
            }
        };
    }

    @Override
    public <T> void putByteMapper(String key, T obj, IByteMapper<T> mapper) {
        putByteMapper(key, obj, C_Illegal_Duration, mapper);
    }

    @Override
    public <T> void putByteMapper(String key, T obj, long duration, IByteMapper<T> mapper) {
        bytesMap.put(key, new CacheEntry(mapper.getBytes(obj), CacheUtils.fixDuration(duration)));
    }

    public <T> void putByteMapper(String key, T obj, long saveTime, long duration, IByteMapper<T> mapper) {
        bytesMap.put(key, new CacheEntry(mapper.getBytes(obj), saveTime, CacheUtils.fixDuration(duration)));
    }

    @Override
    public <T> T getByteMapper(String key, IByteMapper<T> mapper) {
        CacheEntry cacheEntry = bytesMap.get(key);
        if (cacheEntry == null) {
            return null;
        }
        if (CacheUtils.isExpired(cacheEntry.saveTime, cacheEntry.duration)) {
            evict(key);
            return null;
        }
        return mapper.getObject(cacheEntry.bytes);
    }

    @Override
    public boolean isExpired(String key) {
        CacheEntry cacheEntry = bytesMap.get(key);
        return cacheEntry != null && CacheUtils.isExpired(cacheEntry.saveTime, cacheEntry.duration);
    }


    @Override
    public void evict(String key) {
        if (bytesMap.get(key) != null) {
            bytesMap.remove(key);
        }
    }

    @Override
    public void evictAll() {
        bytesMap.evictAll();
    }

    @Override
    public boolean isCached(String key) {
        return bytesMap.get(key) != null;
    }

    @Override
    public long[] getDurationInfo(String key) {
        CacheEntry cacheEntry = bytesMap.get(key);
        if (cacheEntry == null) {
            return null;
        }
        return new long[]{cacheEntry.saveTime, cacheEntry.duration};
    }

    private static class CacheEntry implements Serializable {
        byte[] bytes;
        long saveTime;
        long duration;

        public CacheEntry(byte[] bytes, long duration) {
            this.bytes = bytes;
            this.saveTime = System.currentTimeMillis();
            this.duration = duration;
        }

        public CacheEntry(byte[] bytes, long saveTime, long duration) {
            this.bytes = bytes;
            this.saveTime = saveTime;
            this.duration = duration;
        }
    }
}
