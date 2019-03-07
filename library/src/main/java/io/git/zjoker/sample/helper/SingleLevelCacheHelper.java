package io.git.zjoker.zcache.helper;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.git.zjoker.zcache.ZCacheConfig;
import io.git.zjoker.zcache.converter.IByteConverter;
import io.git.zjoker.zcache.core.ICacheCore;
import io.git.zjoker.zcache.utils.Optional;

public class SingleLevelCacheHelper<V extends ICacheCore> implements ICacheHelper<V> {
    private V cacheCore;

    public SingleLevelCacheHelper(V ICache) {
        this.cacheCore = ICache;
    }

    @Override
    public <T> void put(String key, T obj) {
        put(key, obj, ZCacheConfig.instance().getConverter((Class<T>) obj.getClass()));
    }

    @Override
    public <T> void put(String key, T obj, IByteConverter<T> mapper) {
        put(key, obj, C_Illegal_Duration, mapper);
    }

    @Override
    public <T> void put(String key, T obj, long duration, IByteConverter<T> converter) {
        Optional.checkNotNull(obj, "obj is null !!!");
        Optional.checkNotNull(converter, "mapper is null !!!");
        cacheCore.put(key, obj, duration, converter);
    }

    @Override
    public <T> void putWithDeadLine(String key, T obj, long deadLine, IByteConverter<T> converter) {
        Optional.checkNotNull(obj, "obj is null !!!");
        Optional.checkNotNull(converter, "mapper is null !!!");
        cacheCore.putWithDeadLine(key, obj, deadLine, converter);
    }

    @Override
    public <T> T get(String key, IByteConverter<T> converter) {
        return cacheCore.get(key, converter);
    }

    @Override
    public void putBytes(String key, byte[] bytes) {
        putBytes(key, bytes, C_Illegal_Duration);
    }

    @Override
    public void putBytes(String key, byte[] bytes, long duration) {
        put(key, bytes, duration, ZCacheConfig.instance().getConverter(byte[].class));
    }

    @Override
    public byte[] getBytes(String key) {
        return get(key, ZCacheConfig.instance().getConverter(byte[].class));
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap) {
        putBitmap(key, bitmap, C_Illegal_Duration);
    }

    @Override
    public void putBitmap(String key, Bitmap bitmap, long duration) {
        put(key, bitmap, duration, ZCacheConfig.instance().getConverter(Bitmap.class));
    }

    @Override
    public Bitmap getBitmap(String key) {
        return get(key, ZCacheConfig.instance().getConverter(Bitmap.class));
    }

    @Override
    public void putSerializable(String key, Serializable obj) {
        putSerializable(key, obj, C_Illegal_Duration);
    }

    @Override
    public void putSerializable(String key, Serializable obj, long duration) {
        put(key, obj, duration, ZCacheConfig.instance().getConverter(Serializable.class));
    }

    @Override
    public Serializable getSerializable(String key) {
        return get(key, ZCacheConfig.instance().getConverter(Serializable.class));
    }


    @Override
    public void putJSONObject(String key, JSONObject obj) {
        putJSONObject(key, obj, C_Illegal_Duration);
    }

    @Override
    public void putJSONObject(String key, JSONObject obj, long duration) {
        putString(key, obj.toString(), duration);
    }


    @Override
    public JSONObject getJSONObject(String key) throws JSONException {
        return new JSONObject(getString(key));
    }

    @Override
    public void putString(String key, String obj) {
        putString(key, obj, C_Illegal_Duration);
    }

    @Override
    public void putString(String key, String obj, long duration) {
        put(key, obj, duration, ZCacheConfig.instance().getConverter(String.class));
    }

    @Override
    public String getString(String key) {
        return get(key, ZCacheConfig.instance().getConverter(String.class));
    }

    @Override
    public V getCacheCore() {
        return cacheCore;
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
    public long getDeadLine(String key) {
        return cacheCore.getDeadLine(key);
    }
}
