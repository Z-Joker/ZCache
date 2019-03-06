package io.git.zjoker.zcache.helper;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.git.zjoker.zcache.CacheConfigManager;
import io.git.zjoker.zcache.CacheUtils;
import io.git.zjoker.zcache.core.DiskCacheCore;
import io.git.zjoker.zcache.core.ICacheCore;
import io.git.zjoker.zcache.core.MemoryCacheCore;
import io.git.zjoker.zcache.mapper.IByteConverter;

/**
 * Created by borney on 3/1/17.
 */

public final class DoubleCacheHelper implements ICacheHelper {
    private DiskCacheCore diskCacheCore;
    private MemoryCacheCore memoryCacheCore;

    public DoubleCacheHelper(MemoryCacheCore memoryCacheCore, DiskCacheCore diskCacheCore) {
        this.memoryCacheCore = memoryCacheCore;
        this.diskCacheCore = diskCacheCore;
    }

    @Override
    public void putBytes(String key, byte[] bytes) {
        put(key, bytes, CacheConfigManager.instance().getMapper(byte[].class));
    }

    @Override
    public byte[] getBytes(String key) {
        return get(key, CacheConfigManager.instance().getMapper(byte[].class));
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        put(key, bitmap, CacheConfigManager.instance().getMapper(Bitmap.class));
    }

    @Override
    public Bitmap getBitmap(String key) {
        return get(key, CacheConfigManager.instance().getMapper(Bitmap.class));
    }

    @Override
    public <T extends Serializable> void putSerializable(String key, T obj) {
        put(key, obj, CacheConfigManager.instance().getMapper(Serializable.class));
    }

    @Override
    public <T extends Serializable> void putSerializable(String key, T obj, long duration) {
        put(key, obj, duration, CacheConfigManager.instance().getMapper(Serializable.class));
    }

    @Override
    public <T extends Serializable> T getSerializable(String key) {
        return (T) get(key, CacheConfigManager.instance().getMapper(Serializable.class));
    }

    @Override
    public void putJSONObject(String key, JSONObject obj) {
        putString(key, obj.toString());
    }

    @Override
    public JSONObject getJSONObject(String key) throws JSONException {
        return new JSONObject(getString(key));
    }

    @Override
    public String getString(String key) {
        return get(key, CacheConfigManager.instance().getMapper(String.class));
    }

    @Override
    public void putString(String key, String obj) {
        put(key, obj, CacheConfigManager.instance().getMapper(String.class));
    }

    @Override
    public void putString(String key, String obj, long duration) {
        put(key, obj, duration, CacheConfigManager.instance().getMapper(String.class));
    }

    @Override
    public ICacheCore getCacheCore() {
        return memoryCacheCore != null ? memoryCacheCore : diskCacheCore;
    }

    @Override
    public <T> void putByteMapper(String key, T obj) {
        put(key, obj, CacheConfigManager.instance().getMapper((Class<T>) obj.getClass()));
    }

    @Override
    public <T> void put(String key, T obj, IByteConverter<T> converter) {
        memoryCacheCore.put(key, obj, converter);
        diskCacheCore.put(key, obj, converter);
    }

    @Override
    public <T> void put(String key, T obj, long duration, IByteConverter<T> converter) {
        memoryCacheCore.put(key, obj, duration, converter);
        diskCacheCore.put(key, obj, duration, converter);
    }

    @Override
    public <T> T get(String key, IByteConverter<T> converter) {
        T obj = memoryCacheCore.get(key, converter);
        if (obj == null) {
            obj = diskCacheCore.get(key, converter);
            if (obj != null) {
                long[] durationInfo = diskCacheCore.getDurationInfo(key);
                if (durationInfo != null && CacheUtils.isLegalDuration(durationInfo[1])) {
                    memoryCacheCore.put(key, obj, durationInfo[0], durationInfo[1], converter);
                } else {
                    memoryCacheCore.put(key, obj, converter);
                }
            }
        }
        return obj;
    }

    @Override
    public boolean isExpired(String key) {
        if (memoryCacheCore.isCached(key)) {
            return memoryCacheCore.isExpired(key);
        }
        return diskCacheCore.isExpired(key);
    }

    @Override
    public synchronized void evict(String key) {
        diskCacheCore.evict(key);
        memoryCacheCore.evict(key);
    }

    @Override
    public synchronized void evictAll() {
        diskCacheCore.evictAll();
        memoryCacheCore.evictAll();
    }

    @Override
    public boolean isCached(String key) {
        return memoryCacheCore.isCached(key) || diskCacheCore.isCached(key);
    }

    @Override
    public long[] getDurationInfo(String key) {
        long[] durationInfo = memoryCacheCore.getDurationInfo(key);
        if (durationInfo != null) {
            return durationInfo;
        }
        return diskCacheCore.getDurationInfo(key);
    }

    /**
     * recycle current cache manager and memory cache
     */
    @SuppressWarnings("unused")
    public void recycle() {
        memoryCacheCore.evictAll();
        memoryCacheCore = null;
        diskCacheCore.evictAll();
        memoryCacheCore = null;
    }
}
