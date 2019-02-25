package io.git.zjoker.zcache.helper;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import base.cache.CacheConfigManager;
import base.cache.CacheUtils;
import base.cache.core.DiskCacheCore;
import base.cache.core.ICacheCore;
import base.cache.core.MemoryCacheCore;
import base.cache.mapper.IByteMapper;
import io.git.zjoker.zcache.CacheConfigManager;
import io.git.zjoker.zcache.CacheUtils;
import io.git.zjoker.zcache.core.DiskCacheCore;
import io.git.zjoker.zcache.core.ICacheCore;
import io.git.zjoker.zcache.core.MemoryCacheCore;
import io.git.zjoker.zcache.mapper.IByteMapper;

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
        putByteMapper(key, bytes, CacheConfigManager.instance().getMapper(byte[].class));
    }

    @Override
    public byte[] getBytes(String key) {
        return getByteMapper(key, CacheConfigManager.instance().getMapper(byte[].class));
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        putByteMapper(key, bitmap, CacheConfigManager.instance().getMapper(Bitmap.class));
    }

    @Override
    public Bitmap getBitmap(String key) {
        return getByteMapper(key, CacheConfigManager.instance().getMapper(Bitmap.class));
    }

    @Override
    public <T extends Serializable> void putSerializable(String key, T obj) {
        putByteMapper(key, obj, CacheConfigManager.instance().getMapper(Serializable.class));
    }

    @Override
    public <T extends Serializable> void putSerializable(String key, T obj, long duration) {
        putByteMapper(key, obj, duration, CacheConfigManager.instance().getMapper(Serializable.class));
    }

    @Override
    public <T extends Serializable> T getSerializable(String key) {
        return (T) getByteMapper(key, CacheConfigManager.instance().getMapper(Serializable.class));
    }

    @Override
    public void putJSONObject(String key, JSONObject obj) {
        putSerializable(key, obj.toString());
    }

    @Override
    public JSONObject getJSONObject(String key) throws JSONException {
        return new JSONObject(getString(key));
    }

    @Override
    public String getString(String key) {
        return getByteMapper(key, CacheConfigManager.instance().getMapper(String.class));
    }

    @Override
    public void putString(String key, String obj) {
        putString(key, obj, C_Illegal_Duration);
    }

    @Override
    public void putString(String key, String obj, long duration) {
        putByteMapper(key, obj, duration, CacheConfigManager.instance().getMapper(String.class));
    }

    @Override
    public ICacheCore getCacheCore() {
        return memoryCacheCore != null ? memoryCacheCore : diskCacheCore;
    }

    @Override
    public <T> void putByteMapper(String key, T obj) {
        putByteMapper(key, obj, CacheConfigManager.instance().getMapper((Class<T>) obj.getClass()));
    }

    @Override
    public <T> void putByteMapper(String key, T obj, IByteMapper<T> mapper) {
        putByteMapper(key, obj, C_Illegal_Duration, mapper);
    }

    @Override
    public <T> void putByteMapper(String key, T obj, long age, IByteMapper<T> mapper) {
        memoryCacheCore.putByteMapper(key, obj, age, mapper);
        diskCacheCore.putByteMapper(key, obj, age, mapper);
    }

    @Override
    public <T> T getByteMapper(String key, IByteMapper<T> mapper) {
        T obj = memoryCacheCore.getByteMapper(key, mapper);
        if (obj == null) {
            obj = diskCacheCore.getByteMapper(key, mapper);
            if (obj != null) {
                long[] durationInfo = diskCacheCore.getDurationInfo(key);
                if (durationInfo != null && CacheUtils.isLegalDuration(durationInfo[1])) {
                    memoryCacheCore.putByteMapper(key, obj, durationInfo[0], durationInfo[1], mapper);
                } else {
                    memoryCacheCore.putByteMapper(key, obj, mapper);
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
        diskCacheCore = null;
    }
}
