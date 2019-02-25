package io.git.zjoker.zcache.helper;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import base.cache.CacheConfigManager;
import base.cache.core.ICacheCore;
import base.cache.mapper.BytesMapper;
import base.cache.mapper.IByteMapper;
import base.functional.Optional;
import io.git.zjoker.zcache.CacheConfigManager;
import io.git.zjoker.zcache.Optional;
import io.git.zjoker.zcache.core.ICacheCore;
import io.git.zjoker.zcache.mapper.BytesMapper;
import io.git.zjoker.zcache.mapper.IByteMapper;

public class SingleCacheHelper implements ICacheHelper {
    private ICacheCore cacheCore;

    public SingleCacheHelper(ICacheCore ICache) {
        this.cacheCore = ICache;
    }

    @Override
    public void putBytes(String key, byte[] bytes) {
        putByteMapper(key, bytes, new BytesMapper());
    }

    @Override
    public byte[] getBytes(String key) {
        return getByteMapper(key, new BytesMapper());
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
        putSerializable(key, obj, C_Illegal_Duration);
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
        return new JSONObject((String) getSerializable(key));
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
        return cacheCore;
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
        Optional.checkNotNull(obj, "obj is null !!!");
        Optional.checkNotNull(mapper, "mapper is null !!!");
        cacheCore.putByteMapper(key, obj, age, mapper);
    }

    @Override
    public <T> T getByteMapper(String key, IByteMapper<T> mapper) {
        return cacheCore.getByteMapper(key, mapper);
    }

    @Override
    public boolean isExpired(String key) {
        return cacheCore.isExpired(key);
    }

    @Override
    public synchronized void evict(String key) {
        cacheCore.evict(key);
    }

    @Override
    public synchronized void evictAll() {
        cacheCore.evictAll();
    }

    @Override
    public boolean isCached(String key) {
        return cacheCore.isCached(key);
    }

    @Override
    public long[] getDurationInfo(String key) {
        return cacheCore.getDurationInfo(key);
    }

    /**
     * recycle current cacheCore manager and memory cacheCore
     */
    @SuppressWarnings("unused")
    public void recycle() {
        cacheCore.evictAll();
        cacheCore = null;
    }
}
