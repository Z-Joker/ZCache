package io.git.zjoker.zcache.helper;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.git.zjoker.zcache.ZCacheConfig;
import io.git.zjoker.zcache.converter.IByteConverter;
import io.git.zjoker.zcache.core.DiskCache;
import io.git.zjoker.zcache.core.ICache;
import io.git.zjoker.zcache.core.MemoryCache;


/**
 * 2 Level Cache assembled by Memory cache and Disk cache.
 */
public final class Level2CacheHelper implements ICacheHelper<ICache> {
    private ICacheHelper<MemoryCache> memoryCacheHelper;
    private ICacheHelper<DiskCache> diskCacheHelper;

    public Level2CacheHelper(ICacheHelper<MemoryCache> memoryCacheHelper, ICacheHelper<DiskCache> diskCacheHelper) {
        this.memoryCacheHelper = memoryCacheHelper;
        this.diskCacheHelper = diskCacheHelper;
    }

    @Override
    public <T> void put(String key, T obj) {
        put(key, obj, ZCacheConfig.instance().getConverter((Class<T>) obj.getClass()));
    }

    @Override
    public <T> void put(String key, T obj, IByteConverter<T> converter) {
        put(key, obj, C_Without_Duration, converter);
    }

    @Override
    public <T> void put(String key, T obj, long duration, IByteConverter<T> converter) {
        memoryCacheHelper.put(key, obj, duration, converter);
        diskCacheHelper.put(key, obj, duration, converter);
    }

    @Override
    public <T> void putWithDeadLine(String key, T obj, long deadLine, IByteConverter<T> converter) {
        memoryCacheHelper.putWithDeadLine(key, obj, deadLine, converter);
        diskCacheHelper.putWithDeadLine(key, obj, deadLine, converter);
    }

    @Override
    public <T> T get(String key, IByteConverter<T> converter) {
        T obj = memoryCacheHelper.get(key, converter);
        if (obj == null) {
            obj = diskCacheHelper.get(key, converter);
            if (obj != null) {
                long deadLine = diskCacheHelper.getDeadLine(key);
                if (deadLine >= 0) {
                    memoryCacheHelper.putWithDeadLine(key, obj, deadLine, converter);
                } else {
                    memoryCacheHelper.put(key, obj, C_Without_Duration, converter);
                }
            }
        }
        return obj;
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
        put(key, obj, duration, ZCacheConfig.instance().getConverter(Object.class));
    }

    @Override
    public <T> T getObj(String key, Class<T> tClass) {
        return (T) get(key, ZCacheConfig.instance().getConverter(Object.class));
    }

    @Override
    public boolean isExpired(String key) {
        if (memoryCacheHelper.contains(key)) {
            return memoryCacheHelper.isExpired(key);
        }
        return diskCacheHelper.isExpired(key);
    }

    @Override
    public synchronized void remove(String key) {
        diskCacheHelper.remove(key);
        memoryCacheHelper.remove(key);
    }

    @Override
    public synchronized void removeAll() {
        diskCacheHelper.removeAll();
        memoryCacheHelper.removeAll();
    }

    @Override
    public boolean contains(String key) {
        return memoryCacheHelper.contains(key) || diskCacheHelper.contains(key);
    }

    @Override
    public long getDeadLine(String key) {
        long deadLine = memoryCacheHelper.getDeadLine(key);
        if (deadLine > 0) {
            return deadLine;
        }
        return diskCacheHelper.getDeadLine(key);
    }

    public ICacheHelper memory() {
        return memoryCacheHelper;
    }

    public ICacheHelper disk() {
        return diskCacheHelper;
    }
}
