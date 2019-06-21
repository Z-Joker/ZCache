package io.git.zjoker.zcache.helper;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.git.zjoker.zcache.ZCacheConfig;
import io.git.zjoker.zcache.converter.IByteConverter;
import io.git.zjoker.zcache.core.ICache;
import io.git.zjoker.zcache.utils.Optional;

/**
 * 1 Level Cache only hold Memory cache or Disk cache.
 */
public class Level1CacheHelper<V extends ICache> implements ICacheHelper<V> {
    private V cache;

    public Level1CacheHelper(V ICache) {
        this.cache = ICache;
    }

    @Override
    public <T> void put(String key, T obj) {
        put(key, obj, ZCacheConfig.instance().getConverter((Class<T>) obj.getClass()));
    }

    @Override
    public <T> void put(String key, T obj, IByteConverter<T> mapper) {
        put(key, obj, C_Without_Duration, mapper);
    }

    @Override
    public <T> void put(String key, T obj, long duration, IByteConverter<T> converter) {
        Optional.checkNotNull(obj, "obj is null !!!");
        Optional.checkNotNull(converter, "mapper is null !!!");
        cache.put(key, obj, duration, converter);
    }

    @Override
    public <T> void putWithDeadLine(String key, T obj, long deadLine, IByteConverter<T> converter) {
        Optional.checkNotNull(obj, "obj is null !!!");
        Optional.checkNotNull(converter, "mapper is null !!!");
        cache.putWithDeadLine(key, obj, deadLine, converter);
    }

    @Override
    public <T> T get(String key, IByteConverter<T> converter) {
        return cache.get(key, converter);
    }

    @Override
    public void putBytes(String key, byte[] bytes) {
        putBytes(key, bytes, C_Without_Duration);
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
        putBitmap(key, bitmap, C_Without_Duration);
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
        putSerializable(key, obj, C_Without_Duration);
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
        putJSONObject(key, obj, C_Without_Duration);
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
        putString(key, obj, C_Without_Duration);
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
    public void putObj(String key, Object obj) {
        putObj(key, obj, C_Without_Duration);
    }


    @Override
    public void putObj(String key, Object obj, long duration) {
        put(key, obj, duration, ZCacheConfig.instance().getObjConverter((Class) obj.getClass()));
    }


    @Override
    public <T> T getObj(String key, Class<T> tClass) {
        return get(key, ZCacheConfig.instance().getObjConverter(tClass));
    }

    @Override
    public boolean isExpired(String key) {
        return cache.isExpired(key);
    }

    @Override
    public synchronized void remove(String key) {
        cache.remove(key);
    }

    @Override
    public synchronized void removeAll() {
        cache.removeAll();
    }

    @Override
    public boolean contains(String key) {
        return cache.contains(key);
    }

    @Override
    public long getDeadLine(String key) {
        return cache.getDeadLine(key);
    }
}
