package io.git.zjoker.zcache.helper;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.git.zjoker.zcache.CacheConfigManager;
import io.git.zjoker.zcache.Optional;
import io.git.zjoker.zcache.core.ICacheCore;
import io.git.zjoker.zcache.mapper.BytesMapper;
import io.git.zjoker.zcache.mapper.IByteConverter;

public class SingleCacheHelper implements ICacheHelper {
    private ICacheCore cacheCore;

    public SingleCacheHelper(ICacheCore ICache) {
        this.cacheCore = ICache;
    }

    @Override
    public void putBytes(String key, byte[] bytes) {
        put(key, bytes, new BytesMapper());
    }

    @Override
    public byte[] getBytes(String key) {
        return get(key, new BytesMapper());
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
        return cacheCore;
    }

    @Override
    public <T> void putByteMapper(String key, T obj) {
        put(key, obj, CacheConfigManager.instance().getMapper((Class<T>) obj.getClass()));
    }

    @Override
    public <T> void put(String key, T obj, IByteConverter<T> mapper) {
        put(key, obj, mapper);
    }

    @Override
    public <T> void put(String key, T obj, long duration, IByteConverter<T> converter) {
        Optional.checkNotNull(obj, "obj is null !!!");
        Optional.checkNotNull(converter, "mapper is null !!!");
        cacheCore.put(key, obj, duration, converter);
    }

    @Override
    public <T> T get(String key, IByteConverter<T> converter) {
        return cacheCore.get(key, converter);
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
